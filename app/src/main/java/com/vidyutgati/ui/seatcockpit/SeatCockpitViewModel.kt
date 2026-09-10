package com.vidyutgati.ui.seatcockpit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vidyutgati.core.database.DailyKhataEntity
import com.vidyutgati.core.database.TripEntity
import com.vidyutgati.core.database.VidyutDatabase
import com.vidyutgati.core.soundbox.SoundboxEngine
import com.vidyutgati.domain.model.PaymentApp
import com.vidyutgati.domain.model.PaymentMode
import com.vidyutgati.domain.model.SeatOccupancyState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SeatCockpitViewModel(application: Application) : AndroidViewModel(application) {

    private val db = VidyutDatabase.getInstance(application)
    val soundboxEngine = SoundboxEngine(application)

    private val _seatState = MutableStateFlow(SeatOccupancyState())
    val seatState: StateFlow<SeatOccupancyState> = _seatState.asStateFlow()

    val recentTrips: StateFlow<List<TripEntity>> = db.tripDao()
        .getRecentTrips()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _lastActionMessage = MutableStateFlow<String?>(null)
    val lastActionMessage: StateFlow<String?> = _lastActionMessage.asStateFlow()

    private fun getTodayIso(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    fun deleteTrip(tripId: Long) {
        viewModelScope.launch {
            val trip = db.tripDao().getTripById(tripId)
            if (trip != null) {
                db.tripDao().deleteTrip(tripId)
                val todayIso = getTodayIso()
                db.dailyKhataDao().rollbackTripEarnings(todayIso, trip.fareCollected, trip.passengerCount)
                _lastActionMessage.value = "🗑️ ट्रिप हटाई गई: ₹${trip.fareCollected.toInt()} समायोजित (Rollback)"
            }
        }
    }

    fun incrementSeat() {
        _seatState.update { current ->
            if (current.currentOccupancy < current.maxCapacity) {
                val newCount = current.currentOccupancy + 1
                if (newCount == current.maxCapacity) {
                    _lastActionMessage.value = "गाड़ी फुल हो गई! (All Seats Full)"
                } else {
                    _lastActionMessage.value = "सवारी बैठी ($newCount/${current.maxCapacity})"
                }
                current.copy(currentOccupancy = newCount)
            } else {
                _lastActionMessage.value = "सीटें पहले से फुल हैं!"
                current
            }
        }
    }

    fun fillAllSeats() {
        _seatState.update { current ->
            _lastActionMessage.value = "⚡ पूरी गाड़ी फुल हो गई! (All ${current.maxCapacity} Seats Full)"
            current.copy(currentOccupancy = current.maxCapacity)
        }
    }

    fun decrementSeat() {
        _seatState.update { current ->
            if (current.currentOccupancy > 0) {
                val newCount = current.currentOccupancy - 1
                _lastActionMessage.value = "सवारी उतरी ($newCount/${current.maxCapacity})"
                current.copy(currentOccupancy = newCount)
            } else {
                _lastActionMessage.value = "गाड़ी पहले से खाली है!"
                current
            }
        }
    }

    fun toggleRouteDirection() {
        _seatState.update { current ->
            val toggled = !current.isForwardRoute
            _lastActionMessage.value = if (toggled) {
                "रूट: ${current.routeOrigin} ➔ ${current.routeDestination}"
            } else {
                "रूट: ${current.routeDestination} ➔ ${current.routeOrigin}"
            }
            current.copy(isForwardRoute = toggled)
        }
    }

    fun setFarePerSeat(fare: Double) {
        _seatState.update { it.copy(farePerSeat = fare) }
        _lastActionMessage.value = "किराया सेट: ₹${fare.toInt()}/सवारी"
    }

    fun completeTripAndCollect(mode: PaymentMode) {
        val state = _seatState.value
        val passengers = if (state.currentOccupancy > 0) state.currentOccupancy else state.maxCapacity
        val totalFare = passengers * state.farePerSeat

        viewModelScope.launch {
            val todayIso = getTodayIso()

            // 1. Record Trip
            db.tripDao().insertTrip(
                TripEntity(
                    routeName = state.currentRouteDisplay,
                    timestampEpoch = System.currentTimeMillis(),
                    passengerCount = passengers,
                    fareCollected = totalFare,
                    paymentMode = mode.name
                )
            )

            // 2. Update Daily Khata
            val existing = db.dailyKhataDao().getKhataForDate(todayIso)
            db.dailyKhataDao().addTripEarnings(todayIso, totalFare, passengers)

            // 3. Audio Announcement if UPI
            if (mode == PaymentMode.UPI) {
                soundboxEngine.announcePayment(totalFare, PaymentApp.PAYTM)
            }

            // 4. Reset occupancy for next round trip and flip direction
            _seatState.update { current ->
                current.copy(
                    currentOccupancy = 0,
                    isForwardRoute = !current.isForwardRoute
                )
            }

            _lastActionMessage.value = "✅ ट्रिप पूरी हुई: ₹${totalFare.toInt()} जमा हुए ($mode)"
        }
    }

    override fun onCleared() {
        super.onCleared()
        soundboxEngine.shutdown()
    }
}
