package com.vidyutgati.ui.khata

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vidyutgati.core.database.DailyKhataEntity
import com.vidyutgati.core.database.VidyutDatabase
import com.vidyutgati.domain.model.DailyKhata
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DailyKhataViewModel(application: Application) : AndroidViewModel(application) {

    private val db = VidyutDatabase.getInstance(application)

    val todayDateIso: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val formattedDisplayDate: String = SimpleDateFormat("dd MMMM yyyy", Locale.forLanguageTag("hi")).format(Date())

    val todayKhata: StateFlow<DailyKhata> = db.dailyKhataDao()
        .getKhataForDate(todayDateIso)
        .map { entity ->
            if (entity != null) {
                DailyKhata(
                    dateIso = entity.dateIso,
                    grossEarnings = entity.grossEarnings,
                    thekedarRent = entity.thekedarRent,
                    chargingExpense = entity.chargingExpense,
                    otherExpenses = entity.otherExpenses,
                    totalTrips = entity.totalTrips,
                    totalPassengers = entity.totalPassengers
                )
            } else {
                DailyKhata(dateIso = todayDateIso)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DailyKhata(dateIso = todayDateIso))

    val weeklySummary: StateFlow<WeeklyKhataSummary> = db.dailyKhataDao()
        .getRecentKhatas()
        .map { list ->
            val last7 = list.take(7)
            var gross = 0.0
            var rent = 0.0
            var charging = 0.0
            var other = 0.0
            var passengers = 0

            last7.forEach { item ->
                gross += item.grossEarnings
                rent += item.thekedarRent
                charging += item.chargingExpense
                other += item.otherExpenses
                passengers += item.totalPassengers
            }

            WeeklyKhataSummary(
                totalGross = gross,
                totalNetProfit = gross - rent - charging - other,
                totalBhattaPaid = rent,
                totalChargingPaid = charging,
                totalPassengers = passengers,
                activeDays = last7.size.coerceAtLeast(1)
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), WeeklyKhataSummary())

    fun updateGrossEarnings(addedAmount: Double) {
        viewModelScope.launch {
            val current = todayKhata.value
            val updated = current.copy(grossEarnings = (current.grossEarnings + addedAmount).coerceAtLeast(0.0))
            saveEntity(updated)
        }
    }

    fun updateThekedarRent(rent: Double) {
        viewModelScope.launch {
            val current = todayKhata.value
            val updated = current.copy(thekedarRent = rent.coerceAtLeast(0.0))
            saveEntity(updated)
        }
    }

    fun addChargingExpense(amount: Double) {
        viewModelScope.launch {
            val current = todayKhata.value
            val updated = current.copy(chargingExpense = (current.chargingExpense + amount).coerceAtLeast(0.0))
            saveEntity(updated)
        }
    }

    fun addOtherExpense(amount: Double) {
        viewModelScope.launch {
            val current = todayKhata.value
            val updated = current.copy(otherExpenses = (current.otherExpenses + amount).coerceAtLeast(0.0))
            saveEntity(updated)
        }
    }

    private suspend fun saveEntity(model: DailyKhata) {
        db.dailyKhataDao().insertOrUpdateKhata(
            DailyKhataEntity(
                dateIso = model.dateIso,
                grossEarnings = model.grossEarnings,
                thekedarRent = model.thekedarRent,
                chargingExpense = model.chargingExpense,
                otherExpenses = model.otherExpenses,
                totalTrips = model.totalTrips,
                totalPassengers = model.totalPassengers
            )
        )
    }

    fun resetTodayKhata() {
        viewModelScope.launch {
            db.dailyKhataDao().resetKhata(todayDateIso)
        }
    }

    fun generateWhatsAppSummary(): String {
        val k = todayKhata.value
        val profitEmoji = if (k.isProfitable) "✅" else "⚠️"
        return """
🛺 *विद्युतगति सारथी दैनिक खाता* ($formattedDisplayDate)
━━━━━━━━━━━━━━━━━━
💰 कुल सवारी कमाई: ₹${k.grossEarnings.toInt()} (${k.totalPassengers} सवारियाँ / ${k.totalTrips} चक्कर)
🏢 मालिक का किराया (भत्ता): -₹${k.thekedarRent.toInt()}
⚡ बैटरी चार्जिंग/स्वैप: -₹${k.chargingExpense.toInt()}
🔧 अन्य खर्चे (चाय/पंक्चर): -₹${k.otherExpenses.toInt()}
━━━━━━━━━━━━━━━━━━
$profitEmoji *आज की शुद्ध जेब कमाई: ₹${k.netProfit.toInt()}*
━━━━━━━━━━━━━━━━━━
_विद्युतगति ऐप द्वारा तैयार किया गया (ProNextLabs)_
        """.trimIndent()
    }
}

data class WeeklyKhataSummary(
    val totalGross: Double = 0.0,
    val totalNetProfit: Double = 0.0,
    val totalBhattaPaid: Double = 0.0,
    val totalChargingPaid: Double = 0.0,
    val totalPassengers: Int = 0,
    val activeDays: Int = 1
)
