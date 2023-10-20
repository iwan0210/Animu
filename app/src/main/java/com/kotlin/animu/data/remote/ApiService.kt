package com.kotlin.animu.data.remote

import com.kotlin.animu.data.model.AnimeById
import com.kotlin.animu.data.model.AnimeResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("seasons/now")
    suspend fun getSeasonNow(
        @Query("filter") filter: String = "tv"
    ): AnimeResult

    @GET("anime/{id}")
    suspend fun getAnimeById(
        @Path("id") animeId: Int
    ): AnimeById

    @GET("anime")
    suspend fun searchAnime(
        @Query("q") query: String
    ): AnimeResult

    @GET("anime")
    suspend fun genreAnime(
        @Query("genres") genres: Int,
        @Query("order_by") orderBy: String = "score",
        @Query("sort") sort: String = "desc"
    ): AnimeResult
}