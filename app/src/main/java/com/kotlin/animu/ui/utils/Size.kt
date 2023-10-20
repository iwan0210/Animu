package com.kotlin.animu.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

object Size {
    @Composable
    fun width(): Int {
        val configuration = LocalConfiguration.current
        return configuration.screenWidthDp
    }
}