package com.kotlin.animu.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kotlin.animu.data.model.AnimeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeDao {
    @Query("SELECT * FROM anime")
    fun getFavorites(): Flow<List<AnimeEntity>>

    @Query("SELECT EXISTS(SELECT * FROM anime WHERE malId = :malId)")
    fun checkFavorite(malId: Int): Flow<Boolean>

    @Query("SELECT * FROM anime WHERE title LIKE '%' || :query || '%' OR titleJapanese LIKE '%' || :query || '%'")
    fun searchFavorites(query: String): Flow<List<AnimeEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(anime: AnimeEntity)

    @Delete
    suspend fun delete(anime: AnimeEntity)
}