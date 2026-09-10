package com.vidyutgati.core.i18n

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object IndianCurrencyFormatter {

    /**
     * Formats an amount in Indian Rupee format with ₹ symbol and Indian numbering system
     * e.g. 1250 -> "₹1,250", 150000 -> "₹1,50,000"
     */
    fun formatInr(amount: Double, showDecimals: Boolean = false): String {
        val intPart = amount.toLong()
        val formattedNumber = formatIndianNumber(intPart)
        return if (showDecimals && amount % 1.0 != 0.0) {
            val decimals = String.format(Locale.ENGLISH, ".%02d", ((amount - intPart) * 100).toLong())
            "₹$formattedNumber$decimals"
        } else {
            "₹$formattedNumber"
        }
    }

    fun formatInr(amount: Int): String {
        return formatInr(amount.toDouble())
    }

    private fun formatIndianNumber(number: Long): String {
        if (number < 0) return "-" + formatIndianNumber(-number)
        val s = number.toString()
        if (s.length <= 3) return s

        val last3 = s.substring(s.length - 3)
        val rest = s.substring(0, s.length - 3)

        val sb = StringBuilder()
        var count = 0
        for (i in rest.length - 1 downTo 0) {
            sb.append(rest[i])
            count++
            if (count == 2 && i != 0) {
                sb.append(",")
                count = 0
            }
        }
        return sb.reverse().toString() + "," + last3
    }

    /**
     * Returns natural pronunciation words for standard micro-fare amounts
     * in the selected regional language for the Awaz Box speech engine.
     */
    fun getSpokenAmountWords(amount: Int, language: AppLanguage): String {
        return when (language) {
            AppLanguage.HINDI -> when (amount) {
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
                else -> amount.toString()
            }
            AppLanguage.HINGLISH -> when (amount) {
                5 -> "paanch"
                10 -> "das"
                15 -> "pandrah"
                20 -> "bees"
                25 -> "pachhis"
                30 -> "tees"
                35 -> "paintis"
                40 -> "chalis"
                50 -> "pachas"
                60 -> "saath"
                70 -> "sattar"
                80 -> "assi"
                90 -> "nabbe"
                100 -> "ek sau"
                150 -> "dedh sau"
                200 -> "do sau"
                else -> amount.toString()
            }
            AppLanguage.ENGLISH -> when (amount) {
                5 -> "five"
                10 -> "ten"
                15 -> "fifteen"
                20 -> "twenty"
                25 -> "twenty-five"
                30 -> "thirty"
                35 -> "thirty-five"
                40 -> "forty"
                50 -> "fifty"
                60 -> "sixty"
                70 -> "seventy"
                80 -> "eighty"
                90 -> "ninety"
                100 -> "one hundred"
                150 -> "one hundred fifty"
                200 -> "two hundred"
                else -> amount.toString()
            }
            AppLanguage.BENGALI -> when (amount) {
                5 -> "পাঁচ"
                10 -> "দশ"
                15 -> "পনেরো"
                20 -> "কুড়ি"
                25 -> "পঁচিশ"
                30 -> "ত্রিশ"
                35 -> "পঁয়ত্রিশ"
                40 -> "চল্লিশ"
                50 -> "পঞ্চাশ"
                60 -> "ষাট"
                70 -> "সত্তর"
                80 -> "আশি"
                90 -> "নব্বই"
                100 -> "একশত"
                150 -> "দেড়শত"
                200 -> "দুইশত"
                else -> amount.toString()
            }
            AppLanguage.PUNJABI -> when (amount) {
                5 -> "ਪੰਜ"
                10 -> "ਦਸ"
                15 -> "ਪੰਦਰਾਂ"
                20 -> "ਵੀਹ"
                25 -> "ਪੰਚੀ"
                30 -> "ਤੀਹ"
                35 -> "ਪੈਂਤੀ"
                40 -> "ਚਾਲੀ"
                50 -> "ਪੰਜਾਹ"
                60 -> "ਸੱਠ"
                70 -> "ਸੱਤਰ"
                80 -> "ਅੱਸੀ"
                90 -> "ਨੱਬੇ"
                100 -> "ਇੱਕ ਸੌ"
                else -> amount.toString()
            }
            AppLanguage.GUJARATI -> when (amount) {
                5 -> "પાંચ"
                10 -> "દસ"
                15 -> "પંદર"
                20 -> "વીસ"
                25 -> "પચ્ચીસ"
                30 -> "ત્રીસ"
                40 -> "ચાલીસ"
                50 -> "પચાસ"
                100 -> "એક સો"
                else -> amount.toString()
            }
            AppLanguage.MARATHI -> when (amount) {
                5 -> "पाच"
                10 -> "दहा"
                15 -> "पंधरा"
                20 -> "वीस"
                25 -> "पंचवीस"
                30 -> "तीस"
                40 -> "चाळीस"
                50 -> "पन्नास"
                100 -> "शंभर"
                else -> amount.toString()
            }
            AppLanguage.TAMIL -> when (amount) {
                5 -> "ஐந்து"
                10 -> "பத்து"
                15 -> "பதினைந்து"
                20 -> "இருபது"
                25 -> "இருபத்தைந்து"
                30 -> "முப்பது"
                40 -> "நாற்பது"
                50 -> "ஐம்பது"
                100 -> "நூறு"
                else -> amount.toString()
            }
            AppLanguage.TELUGU -> when (amount) {
                5 -> "ఐదు"
                10 -> "పది"
                15 -> "పదిహేను"
                20 -> "ఇరవై"
                25 -> "ఇరవై ఐదు"
                30 -> "ముప్పై"
                40 -> "నలభై"
                50 -> "యాభై"
                100 -> "వంద"
                else -> amount.toString()
            }
        }
    }

    /**
     * Returns the localized currency unit term (e.g. रुपये, rupaye, rupees, টাকা, etc.)
     */
    fun getCurrencyUnitWord(language: AppLanguage): String {
        return when (language) {
            AppLanguage.HINDI, AppLanguage.MARATHI, AppLanguage.GUJARATI, AppLanguage.PUNJABI -> "रुपये"
            AppLanguage.HINGLISH -> "rupaye"
            AppLanguage.ENGLISH -> "rupees"
            AppLanguage.BENGALI -> "টাকা"
            AppLanguage.TAMIL -> "ரூபாய்"
            AppLanguage.TELUGU -> "రూపాయలు"
        }
    }
}
