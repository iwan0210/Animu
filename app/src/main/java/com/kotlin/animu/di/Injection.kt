package com.kotlin.animu.di

import android.content.Context
import com.kotlin.animu.data.AnimeRepository
import com.kotlin.animu.data.local.AnimeDatabase
import com.kotlin.animu.data.remote.ApiConfig

object Injection {
    fun provideRepository(context: Context): AnimeRepository {
        val apiService = ApiConfig.getApiService()
        val animeDao = AnimeDatabase.getDatabase(context)
        return AnimeRepository(apiService, animeDao.animeDao())
    }
}