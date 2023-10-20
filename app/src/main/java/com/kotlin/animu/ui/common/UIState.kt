package com.kotlin.animu.ui.common

sealed class UIState<out T : Any?> {
    data object Loading : UIState<Nothing>()

    data class Success<out T : Any>(val data: T) : UIState<T>()

    data class Error(val error: String) : UIState<Nothing>()
}
