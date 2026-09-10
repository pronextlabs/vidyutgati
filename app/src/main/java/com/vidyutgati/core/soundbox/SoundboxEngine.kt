package com.vidyutgati.core.soundbox

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import com.vidyutgati.domain.model.PaymentApp
import java.util.Locale

class SoundboxEngine(private val context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false
    private var isHindiSupported = false

    init {
        tts = TextToSpeech(context.applicationContext, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val hindiLocale = Locale("hi", "IN")
            val langResult = tts?.setLanguage(hindiLocale)
            isHindiSupported = langResult != TextToSpeech.LANG_MISSING_DATA &&
                    langResult != TextToSpeech.LANG_NOT_SUPPORTED

            if (!isHindiSupported) {
                // Fallback to English (India) or default
                tts?.language = Locale("en", "IN")
            }
            tts?.setSpeechRate(0.95f) // Slightly slower for crisp intelligibility over traffic noise
            tts?.setPitch(1.0f)
            isInitialized = true
        } else {
            Log.e("SoundboxEngine", "TextToSpeech initialization failed with status $status")
        }
    }

    /**
     * Converts common Indian rupee denominations to natural spoken Hindi words.
     */
    fun convertAmountToHindiWords(amount: Int): String {
        return when (amount) {
            5 -> "पांच"
            10 -> "दस"
            15 -> "पंद्रह"
            20 -> "बीस"
            25 -> "पच्चीस"
            30 -> "तीस"
            35 -> "पैंतीस"
            40 -> "चालीस"
            50 -> "पचास"
            60 -> "साठ"
            70 -> "सत्तर"
            80 -> "अस्सी"
            90 -> "नब्बे"
            100 -> "एक सौ"
            150 -> "एक सौ पचास"
            200 -> "दो सौ"
            250 -> "ढाई सौ"
            300 -> "तीन सौ"
            500 -> "पांच सौ"
            else -> amount.toString()
        }
    }

    /**
     * Announces received payment loudly in authentic soundbox style.
     */
    fun announcePayment(amount: Double, appSource: PaymentApp) {
        if (!isInitialized) return

        val intAmount = amount.toInt()
        val speechText = if (isHindiSupported) {
            val hindiAmount = convertAmountToHindiWords(intAmount)
            "${appSource.hindiName} पर $hindiAmount रुपये प्राप्त हुए।"
        } else {
            "Received $intAmount rupees on ${appSource.displayName}."
        }

        tts?.speak(speechText, TextToSpeech.QUEUE_FLUSH, null, "PAYMENT_ANNOUNCEMENT_${System.currentTimeMillis()}")
    }

    /**
     * Announces a custom battery warning or system alert.
     */
    fun speakAlert(messageHindi: String, messageEnglish: String) {
        if (!isInitialized) return
        val text = if (isHindiSupported) messageHindi else messageEnglish
        tts?.speak(text, TextToSpeech.QUEUE_ADD, null, "ALERT_${System.currentTimeMillis()}")
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        isInitialized = false
    }
}
