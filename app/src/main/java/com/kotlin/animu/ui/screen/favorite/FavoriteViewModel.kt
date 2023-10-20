package com.kotlin.animu.ui.screen.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.animu.data.AnimeRepository
import com.kotlin.animu.data.model.AnimeEntity
import com.kotlin.animu.ui.common.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class FavoriteViewModel(
    val repository: AnimeRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<UIState<List<AnimeEntity>>> =
        MutableStateFlow(UIState.Loading)
    open val uiState: StateFlow<UIState<List<AnimeEntity>>>
        get() = _uiState

    open fun getFavorites() {
        viewModelScope.launch {
            repository.getFavorites()
                .collect {
                    _uiState.value = it
                }
        }
    }

    fun searchFavorite(query: String) {
        viewModelScope.launch {
            repository.searchFavorite(query)
                .collect {
                    _uiState.value = it
                }
        }
    }
}