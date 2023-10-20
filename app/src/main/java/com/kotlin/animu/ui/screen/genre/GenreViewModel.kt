package com.kotlin.animu.ui.screen.genre

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.animu.data.AnimeRepository
import com.kotlin.animu.data.model.AnimeItem
import com.kotlin.animu.ui.common.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GenreViewModel(
    private val repository: AnimeRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<UIState<List<AnimeItem>>> =
        MutableStateFlow(UIState.Loading)
    val uiState: StateFlow<UIState<List<AnimeItem>>>
        get() = _uiState

    fun genreAnime(genreId: Int) {
        viewModelScope.launch {
            repository.genreAnime(genreId)
                .collect {
                    _uiState.value = it
                }
        }
    }
}