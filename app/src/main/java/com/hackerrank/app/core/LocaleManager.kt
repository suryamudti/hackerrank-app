package com.hackerrank.app.core

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocaleManager
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) {
        private val prefs = context.getSharedPreferences("locale_prefs", Context.MODE_PRIVATE)
        private val _currentLocale = MutableStateFlow(currentCode())

        val currentLocale: StateFlow<String> = _currentLocale.asStateFlow()

        private fun currentCode(): String {
            return prefs.getString("locale", "en") ?: "en"
        }

        fun getCurrentCode(): String = currentCode()

        fun setLocale(code: String) {
            prefs.edit().putString("locale", code).apply()
            _currentLocale.value = code
        }

        fun isEnglish(): Boolean = currentCode() == "en"

        companion object {
            fun getLocaleFromContext(context: Context): Locale {
                val prefs = context.getSharedPreferences("locale_prefs", Context.MODE_PRIVATE)
                val code = prefs.getString("locale", "en") ?: "en"
                return Locale(code)
            }
        }
    }
