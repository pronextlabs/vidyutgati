package com.vidyutgati.core.i18n

import java.util.Locale

enum class AppLanguage(
    val code: String,
    val nativeName: String,
    val englishName: String,
    val flag: String,
    val locale: Locale,
    val currencyWord: String
) {
    HINDI(
        code = "hi",
        nativeName = "हिंदी",
        englishName = "Hindi",
        flag = "🇮🇳",
        locale = Locale("hi", "IN"),
        currencyWord = "रुपये"
    ),
    HINGLISH(
        code = "hinglish",
        nativeName = "हिंग्लिश (Hinglish)",
        englishName = "Hinglish",
        flag = "🇮🇳",
        locale = Locale("en", "IN"),
        currencyWord = "rupaye"
    ),
    ENGLISH(
        code = "en",
        nativeName = "English",
        englishName = "English",
        flag = "🌐",
        locale = Locale("en", "US"),
        currencyWord = "rupees"
    ),
    BENGALI(
        code = "bn",
        nativeName = "বাংলা",
        englishName = "Bengali",
        flag = "🇮🇳",
        locale = Locale("bn", "IN"),
        currencyWord = "টাকা"
    ),
    PUNJABI(
        code = "pa",
        nativeName = "ਪੰਜਾਬੀ",
        englishName = "Punjabi",
        flag = "🇮🇳",
        locale = Locale("pa", "IN"),
        currencyWord = "ਰੁਪਏ"
    ),
    GUJARATI(
        code = "gu",
        nativeName = "ગુજરાતી",
        englishName = "Gujarati",
        flag = "🇮🇳",
        locale = Locale("gu", "IN"),
        currencyWord = "રૂપિયા"
    ),
    MARATHI(
        code = "mr",
        nativeName = "मराठी",
        englishName = "Marathi",
        flag = "🇮🇳",
        locale = Locale("mr", "IN"),
        currencyWord = "रुपये"
    ),
    TAMIL(
        code = "ta",
        nativeName = "தமிழ்",
        englishName = "Tamil",
        flag = "🇮🇳",
        locale = Locale("ta", "IN"),
        currencyWord = "ரூபாய்"
    ),
    TELUGU(
        code = "te",
        nativeName = "తెలుగు",
        englishName = "Telugu",
        flag = "🇮🇳",
        locale = Locale("te", "IN"),
        currencyWord = "రూపాయలు"
    );

    companion object {
        fun fromCode(code: String): AppLanguage {
            return entries.find { it.code.equals(code, ignoreCase = true) } ?: HINDI
        }
    }
}
