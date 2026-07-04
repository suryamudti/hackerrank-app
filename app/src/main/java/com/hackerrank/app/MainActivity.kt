package com.hackerrank.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.hackerrank.app.core.navigation.NavGraph
import com.hackerrank.app.core.theme.HackerRankTheme
import com.hackerrank.app.data.seed.SeedInitializer
import com.hackerrank.app.domain.usecase.RecordLoginUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var seedInitializer: SeedInitializer

    @Inject
    lateinit var recordLoginUseCase: RecordLoginUseCase

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize seed data and record login
        scope.launch {
            seedInitializer.initializeIfNeeded()
            recordLoginUseCase()
        }

        setContent {
            HackerRankTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NavGraph()
                }
            }
        }
    }
}
