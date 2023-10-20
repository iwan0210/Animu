package com.kotlin.animu.ui.components

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@Composable
fun LoadingContent(
    modifier: Modifier = Modifier
) {
    CircularProgressIndicator(
        modifier = modifier
            .testTag("loading"),
        color = MaterialTheme.colorScheme.primary
    )
}