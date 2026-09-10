package com.vidyutgati

import com.vidyutgati.core.i18n.AppLanguage
import com.vidyutgati.core.i18n.IndianCurrencyFormatter
import com.vidyutgati.core.i18n.LocalizedStrings
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LanguageAndCurrencyTest {

    @Test
    fun testIndianCurrencyFormatting() {
        // Indian number formatting: comma after thousands, then every 2 digits (lakhs, crores)
        assertEquals("₹15", IndianCurrencyFormatter.formatInr(15.0))
        assertEquals("₹1,250", IndianCurrencyFormatter.formatInr(1250.0))
        assertEquals("₹1,50,000", IndianCurrencyFormatter.formatInr(150000.0))
    }

    @Test
    fun testSpokenAmountInAllLanguages() {
        for (lang in AppLanguage.values()) {
            val words = IndianCurrencyFormatter.getSpokenAmountWords(20, lang)
            val currencyWord = IndianCurrencyFormatter.getCurrencyUnitWord(lang)
            assertNotNull("Spoken words for 20 in $lang should not be null", words)
            assertTrue("Spoken words for 20 in $lang should not be empty", words.isNotBlank())
            assertTrue("Currency word in $lang should not be empty", currencyWord.isNotBlank())
        }
    }

    @Test
    fun testBengaliTakaWord() {
        val bengaliWords = IndianCurrencyFormatter.getSpokenAmountWords(50, AppLanguage.BENGALI)
        val currencyWord = IndianCurrencyFormatter.getCurrencyUnitWord(AppLanguage.BENGALI)
        assertEquals("পঞ্চাশ", bengaliWords)
        assertEquals("টাকা", currencyWord)
    }

    @Test
    fun testAllLanguagesHaveCompleteCatalog() {
        for (lang in AppLanguage.values()) {
            val strings = LocalizedStrings.get(lang)
            assertNotNull(strings.appTitle)
            assertNotNull(strings.tabSeats)
            assertNotNull(strings.tabSoundbox)
            assertNotNull(strings.tabKhata)
            assertNotNull(strings.tabBattery)
            assertNotNull(strings.activeRoute)
            assertNotNull(strings.rushHourQuickFill)
            assertNotNull(strings.soundboxTitle)
            assertNotNull(strings.netProfitTitle)
            assertNotNull(strings.weeklyOverviewTitle)
        }
    }
}
