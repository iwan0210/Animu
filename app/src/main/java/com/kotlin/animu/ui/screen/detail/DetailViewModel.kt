package com.kotlin.animu.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.animu.data.AnimeRepository
import com.kotlin.animu.data.model.Anime
import com.kotlin.animu.data.model.AnimeEntity
import com.kotlin.animu.ui.common.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: AnimeRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<UIState<Anime>> =
        MutableStateFlow(UIState.Loading)
    val uiState: StateFlow<UIState<Anime>>
        get() = _uiState

    private val _isChecked: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val isChecked: StateFlow<Boolean>
        get() = _isChecked

    fun getAnimeById(malId: Int) {
        viewModelScope.launch {
            repository.getAnimeById(malId)
                .collect {
                    _uiState.value = it
                }
        }
    }

    fun checkFavorite(malId: Int) {
        viewModelScope.launch {
            repository.checkFavorite(malId)
                .distinctUntilChanged()
                .collect {
                    _isChecked.value = it
                }
        }
    }

    fun insert(anime: AnimeEntity) {
        viewModelScope.launch {
            repository.insert(anime)
        }
    }

    fun delete(anime: AnimeEntity) {
        viewModelScope.launch {
            repository.delete(anime)
        }
    }
}