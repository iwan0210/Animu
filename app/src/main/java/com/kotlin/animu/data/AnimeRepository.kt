package com.kotlin.animu.data

import com.google.gson.Gson
import com.kotlin.animu.data.local.AnimeDao
import com.kotlin.animu.data.model.Anime
import com.kotlin.animu.data.model.AnimeEntity
import com.kotlin.animu.data.model.AnimeItem
import com.kotlin.animu.data.model.Error
import com.kotlin.animu.data.remote.ApiService
import com.kotlin.animu.ui.common.UIState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class AnimeRepository(
    private val apiService: ApiService,
    private val animeDao: AnimeDao
) {
    fun getSeasonNow(): Flow<UIState<List<AnimeItem>>> = flow {
        emit(UIState.Loading)
        try {
            val response = apiService.getSeasonNow()
            emit(UIState.Success(response.data))
        } catch (e: Exception) {
            if (e is HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, Error::class.java)
                emit(UIState.Error(errorBody.message))
            } else {
                emit(UIState.Error(e.message.toString()))
            }
        }
    }

    fun getAnimeById(animeId: Int): Flow<UIState<Anime>> = flow {
        emit(UIState.Loading)
        try {
            val response = apiService.getAnimeById(animeId)
            emit(UIState.Success(response.data))
        } catch (e: Exception) {
            if (e is HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, Error::class.java)
                emit(UIState.Error(errorBody.message))
            } else {
                emit(UIState.Error(e.message.toString()))
            }
        }
    }

    fun searchAnime(query: String): Flow<UIState<List<AnimeItem>>> = flow {
        emit(UIState.Loading)
        try {
            val response = apiService.searchAnime(query)
            emit(UIState.Success(response.data))
        } catch (e: Exception) {
            if (e is HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, Error::class.java)
                emit(UIState.Error(errorBody.message))
            } else {
                emit(UIState.Error(e.message.toString()))
            }
        }
    }

    fun genreAnime(genreId: Int): Flow<UIState<List<AnimeItem>>> = flow {
        emit(UIState.Loading)
        try {
            val response = apiService.genreAnime(genreId)
            emit(UIState.Success(response.data))
        } catch (e: Exception) {
            if (e is HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, Error::class.java)
                emit(UIState.Error(errorBody.message))
            } else {
                emit(UIState.Error(e.message.toString()))
            }
        }
    }

    fun getFavorites(): Flow<UIState<List<AnimeEntity>>> = flow {
        emit(UIState.Loading)
        animeDao.getFavorites()
            .catch {
                emit(UIState.Error(it.message.toString()))
            }
            .collect { anime ->
                emit(UIState.Success(anime))
            }
    }

    fun searchFavorite(query: String): Flow<UIState<List<AnimeEntity>>> = flow {
        emit(UIState.Loading)
        animeDao.searchFavorites(query)
            .catch {
                emit(UIState.Error(it.message.toString()))
            }
            .collect { anime ->
                emit(UIState.Success(anime))
            }
    }

    fun checkFavorite(malId: Int): Flow<Boolean> =
        animeDao.checkFavorite(malId)

    suspend fun insert(anime: AnimeEntity) = animeDao.insert(anime)

    suspend fun delete(anime: AnimeEntity) = animeDao.delete(anime)
}