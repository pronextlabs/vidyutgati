package com.vidyutgati.core.soundbox

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.vidyutgati.core.database.PaymentNotificationEntity
import com.vidyutgati.core.database.VidyutDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class UpiSoundboxNotificationListener : NotificationListenerService() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var soundboxEngine: SoundboxEngine? = null

    override fun onCreate() {
        super.onCreate()
        soundboxEngine = SoundboxEngine(this)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        if (sbn == null) return

        val packageName = sbn.packageName ?: return
        val extras = sbn.notification?.extras ?: return

        val title = extras.getString(Notification.EXTRA_TITLE)
        val text = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString()

        val payment = UpiNotificationParser.parse(packageName, title, text)
        if (payment != null) {
            Log.i("UpiSoundbox", "Detected incoming payment: ₹${payment.amount} via ${payment.app.displayName}")

            // 1. Loudly announce via TTS in Hindi
            soundboxEngine?.announcePayment(payment.amount, payment.app)

            // 2. Persist to Room Database & increment today's gross earnings
            serviceScope.launch {
                val db = VidyutDatabase.getInstance(applicationContext)
                db.paymentNotificationDao().insertPayment(
                    PaymentNotificationEntity(
                        id = UUID.randomUUID().toString(),
                        amount = payment.amount,
                        appSource = payment.app.displayName,
                        timestampEpoch = System.currentTimeMillis()
                    )
                )

                val todayIso = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                db.dailyKhataDao().addTripEarnings(todayIso, payment.amount, passengers = 1)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        soundboxEngine?.shutdown()
        soundboxEngine = null
    }
}
