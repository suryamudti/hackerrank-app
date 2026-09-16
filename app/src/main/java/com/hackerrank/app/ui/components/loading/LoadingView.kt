package com.hackerrank.app.ui.components.loading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

/**
 * Standardized full-screen or boxed loading view with consistent testTag for integration and UI tests.
 */
@Composable
fun LoadingView(
    modifier: Modifier = Modifier,
    testTag: String = "loadingIndicator",
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(modifier = Modifier.testTag(testTag))
    }
}
