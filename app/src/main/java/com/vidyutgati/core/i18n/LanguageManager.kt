package com.vidyutgati.core.i18n

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LanguageManager private constructor(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("vidyutgati_i18n_prefs", Context.MODE_PRIVATE)

    private val _currentLanguage = MutableStateFlow(loadSavedLanguage())
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    private val _strings = MutableStateFlow(LocalizedStrings.get(_currentLanguage.value))
    val strings: StateFlow<UiStrings> = _strings.asStateFlow()

    private fun loadSavedLanguage(): AppLanguage {
        val savedCode = prefs.getString("selected_language_code", AppLanguage.HINDI.code) ?: AppLanguage.HINDI.code
        return AppLanguage.fromCode(savedCode)
    }

    fun setLanguage(language: AppLanguage) {
        _currentLanguage.value = language
        _strings.value = LocalizedStrings.get(language)
        prefs.edit().putString("selected_language_code", language.code).apply()
    }

    companion object {
        @Volatile
        private var INSTANCE: LanguageManager? = null

        fun getInstance(context: Context): LanguageManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: LanguageManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
