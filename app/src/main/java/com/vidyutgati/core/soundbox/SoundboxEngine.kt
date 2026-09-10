package com.vidyutgati.core.soundbox

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import com.vidyutgati.core.i18n.AppLanguage
import com.vidyutgati.core.i18n.IndianCurrencyFormatter
import com.vidyutgati.core.i18n.LanguageManager
import com.vidyutgati.domain.model.PaymentApp
import java.util.Locale

class SoundboxEngine(private val context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false
    private val languageManager = LanguageManager.getInstance(context)

    init {
        tts = TextToSpeech(context.applicationContext, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            applyLanguageVoice(languageManager.currentLanguage.value)
            tts?.setSpeechRate(0.95f) // Slightly slower for crisp intelligibility over traffic noise
            tts?.setPitch(1.0f)
            isInitialized = true
        } else {
            Log.e("SoundboxEngine", "TextToSpeech initialization failed with status $status")
        }
    }

    private fun applyLanguageVoice(language: AppLanguage) {
        val targetLocale = language.locale
        val result = tts?.setLanguage(targetLocale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            // Fallback to Hindi (India) or English (India)
            val fallback = if (tts?.setLanguage(Locale("hi", "IN")) != TextToSpeech.LANG_NOT_SUPPORTED) {
                Locale("hi", "IN")
            } else {
                Locale("en", "IN")
            }
            tts?.language = fallback
        }
    }

    /**
     * Announces received payment loudly in the driver's chosen language with INR currency.
     */
    fun announcePayment(amount: Double, appSource: PaymentApp, explicitLanguage: AppLanguage? = null) {
        if (!isInitialized) return

        val language = explicitLanguage ?: languageManager.currentLanguage.value
        applyLanguageVoice(language)

        val intAmount = amount.toInt()
        val amountWords = IndianCurrencyFormatter.getSpokenAmountWords(intAmount, language)

        val speechText = when (language) {
            AppLanguage.HINDI -> "${appSource.hindiName} पर $amountWords रुपये प्राप्त हुए।"
            AppLanguage.HINGLISH -> "${appSource.displayName} par $amountWords rupaye receive hue."
            AppLanguage.ENGLISH -> "Received $amountWords rupees on ${appSource.displayName}."
            AppLanguage.BENGALI -> "${appSource.displayName}-এ $amountWords টাকা পাওয়া গেছে।"
            AppLanguage.PUNJABI -> "${appSource.displayName} ਤੇ $amountWords ਰੁਪਏ ਪ੍ਰਾਪਤ ਹੋਏ।"
            AppLanguage.GUJARATI -> "${appSource.displayName} પર $amountWords રૂપિયા મળ્યા."
            AppLanguage.MARATHI -> "${appSource.displayName} वर $amountWords रुपये मिळाले."
            AppLanguage.TAMIL -> "${appSource.displayName}-இல் $amountWords ரூபாய் பெறப்பட்டது."
            AppLanguage.TELUGU -> "${appSource.displayName}-లో $amountWords రూపాయలు వచ్చాయి."
        }

        tts?.speak(speechText, TextToSpeech.QUEUE_FLUSH, null, "PAYMENT_ANNOUNCEMENT_${System.currentTimeMillis()}")
    }

    /**
     * Announces battery warning or system alert in the selected language.
     */
    fun speakAlert(messageHindi: String, messageEnglish: String) {
        if (!isInitialized) return
        val currentLang = languageManager.currentLanguage.value
        applyLanguageVoice(currentLang)
        val text = if (currentLang == AppLanguage.ENGLISH) messageEnglish else messageHindi
        tts?.speak(text, TextToSpeech.QUEUE_ADD, null, "ALERT_${System.currentTimeMillis()}")
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        isInitialized = false
    }
}
