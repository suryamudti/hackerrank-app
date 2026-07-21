package com.hackerrank.app.core

import androidx.compose.runtime.staticCompositionLocalOf

val LocalThemeManager =
    staticCompositionLocalOf<ThemeManager> {
        error("No ThemeManager provided")
    }

val LocalLocaleManager =
    staticCompositionLocalOf<LocaleManager> {
        error("No LocaleManager provided")
    }
