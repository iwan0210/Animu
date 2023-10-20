package com.kotlin.animu.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.animu.data.AnimeRepository
import com.kotlin.animu.data.model.AnimeItem
import com.kotlin.animu.ui.common.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: AnimeRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<UIState<List<AnimeItem>>> =
        MutableStateFlow(UIState.Loading)
    val uiState: StateFlow<UIState<List<AnimeItem>>>
        get() = _uiState

    init {
        getSeasonNow()
    }

    fun getSeasonNow() {
        viewModelScope.launch {
            repository.getSeasonNow()
                .collect {
                    _uiState.value = it
                }
        }
    }

    fun searchAnime(query: String) {
        viewModelScope.launch {
            repository.searchAnime(query)
                .collect {
                    _uiState.value = it
                }
        }
    }
}