package com.kotlin.animu.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kotlin.animu.data.model.AnimeEntity

@Database(
    entities = [AnimeEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AnimeDatabase : RoomDatabase() {
    abstract fun animeDao(): AnimeDao

    companion object {
        @Volatile
        private var Instance: AnimeDatabase? = null

        fun getDatabase(context: Context): AnimeDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AnimeDatabase::class.java, "anime_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}