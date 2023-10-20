package com.kotlin.animu.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kotlin.animu.data.AnimeRepository
import com.kotlin.animu.di.Injection
import com.kotlin.animu.ui.screen.detail.DetailViewModel
import com.kotlin.animu.ui.screen.favorite.FavoriteViewModel
import com.kotlin.animu.ui.screen.genre.GenreViewModel
import com.kotlin.animu.ui.screen.home.HomeViewModel

class ViewModelFactory(
    private val repository: AnimeRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(GenreViewModel::class.java)) {
            return GenreViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}