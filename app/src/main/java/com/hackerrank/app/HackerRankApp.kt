package com.hackerrank.app

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.hackerrank.app.core.LocaleManager
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale

@HiltAndroidApp
class HackerRankApp : Application() {
    override fun attachBaseContext(newBase: Context) {
        val locale = LocaleManager.getLocaleFromContext(newBase)
        Locale.setDefault(locale)
        val config = Configuration(newBase.resources.configuration)
        config.setLocale(locale)
        super.attachBaseContext(newBase.createConfigurationContext(config))
    }
}
