package com.kotlin.animu.ui.screen.favorite

import androidx.lifecycle.viewModelScope
import com.kotlin.animu.data.AnimeRepository
import com.kotlin.animu.data.model.AnimeEntity
import com.kotlin.animu.ui.common.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MockFavoriteViewModel(
    repository: AnimeRepository,
    private val isError: Boolean
) : FavoriteViewModel(repository) {
    private val _uiState: MutableStateFlow<UIState<List<AnimeEntity>>> =
        MutableStateFlow(UIState.Loading)
    override val uiState: StateFlow<UIState<List<AnimeEntity>>>
        get() = _uiState

    override fun getFavorites() {
        if (isError) {
            _uiState.value = UIState.Error("Error Message")
        } else {
            viewModelScope.launch {
                repository.getFavorites()
                    .collect {
                        _uiState.value = it
                    }
            }
        }

    }
}