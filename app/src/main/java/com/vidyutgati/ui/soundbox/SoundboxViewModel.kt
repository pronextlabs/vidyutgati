package com.vidyutgati.ui.soundbox

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vidyutgati.core.database.PaymentNotificationEntity
import com.vidyutgati.core.database.VidyutDatabase
import com.vidyutgati.core.soundbox.SoundboxEngine
import com.vidyutgati.domain.model.PaymentApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class SoundboxViewModel(application: Application) : AndroidViewModel(application) {

    private val db = VidyutDatabase.getInstance(application)
    val soundboxEngine = SoundboxEngine(application)

    val recentPayments: StateFlow<List<PaymentNotificationEntity>> = db.paymentNotificationDao()
        .getRecentPayments()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedApp = MutableStateFlow(PaymentApp.PAYTM)
    val selectedApp: StateFlow<PaymentApp> = _selectedApp.asStateFlow()

    private val _lastAnnouncedAmount = MutableStateFlow<Double?>(null)
    val lastAnnouncedAmount: StateFlow<Double?> = _lastAnnouncedAmount.asStateFlow()

    val upiId = "driver.vidyutgati@upi"

    fun selectApp(app: PaymentApp) {
        _selectedApp.value = app
    }

    fun triggerPaymentAnnouncement(amount: Double) {
        val app = _selectedApp.value
        _lastAnnouncedAmount.value = amount

        // 1. Speak aloud via TTS in Hindi
        soundboxEngine.announcePayment(amount, app)

        // 2. Persist in Database
        viewModelScope.launch {
            db.paymentNotificationDao().insertPayment(
                PaymentNotificationEntity(
                    id = UUID.randomUUID().toString(),
                    amount = amount,
                    appSource = app.displayName,
                    timestampEpoch = System.currentTimeMillis()
                )
            )
        }
    }

    fun repeatLastAnnouncement() {
        val amount = _lastAnnouncedAmount.value ?: 15.0
        val app = _selectedApp.value
        soundboxEngine.announcePayment(amount, app)
    }

    fun deletePayment(id: String) {
        viewModelScope.launch {
            db.paymentNotificationDao().deletePayment(id)
        }
    }

    override fun onCleared() {
        super.onCleared()
        soundboxEngine.shutdown()
    }
}
