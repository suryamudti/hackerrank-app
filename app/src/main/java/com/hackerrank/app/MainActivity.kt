package com.hackerrank.app

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.hackerrank.app.core.LocaleManager
import com.hackerrank.app.core.ThemeManager
import com.hackerrank.app.core.navigation.NavGraph
import com.hackerrank.app.core.theme.HackerRankTheme
import com.hackerrank.app.data.seed.SeedInitializer
import com.hackerrank.app.domain.usecase.RecordLoginUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var seedInitializer: SeedInitializer

    @Inject
    lateinit var recordLoginUseCase: RecordLoginUseCase

    @Inject
    lateinit var themeManager: ThemeManager

    @Inject
    lateinit var localeManager: LocaleManager

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun attachBaseContext(newBase: Context) {
        val locale = LocaleManager.getLocaleFromContext(newBase)
        Locale.setDefault(locale)
        val config = Configuration(newBase.resources.configuration)
        config.setLocale(locale)
        super.attachBaseContext(newBase.createConfigurationContext(config))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        scope.launch {
            seedInitializer.initializeIfNeeded()
            recordLoginUseCase()
        }

        setContent {
            val localeCode by localeManager.currentLocale.collectAsState()

            LaunchedEffect(localeCode) {
                if (localeCode != LocaleManager.getLocaleFromContext(this@MainActivity).language) {
                    this@MainActivity.recreate()
                }
            }

            HackerRankTheme(themeManager = themeManager) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NavGraph(
                        themeManager = themeManager,
                        localeManager = localeManager
                    )
                }
            }
        }
    }
}
