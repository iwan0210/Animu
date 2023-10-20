package com.kotlin.animu.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class AnimeResult(
    @field:SerializedName("data")
    val data: List<AnimeItem>
)

data class AnimeById(
    @field:SerializedName("data")
    val data: Anime
)

@Entity(tableName = "anime")
data class AnimeEntity(
    @PrimaryKey(autoGenerate = false)
    val malId: Int,
    val images: String,
    val title: String,
    val titleJapanese: String,
    val status: String,
    val score: Double? = null,
    val season: String? = null,
    val year: Int? = null
)


data class AnimeItem(
    @field:SerializedName("mal_id")
    val malId: Int,

    @field:SerializedName("images")
    val images: Images,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("title_japanese")
    val titleJapanese: String,

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("score")
    val score: Double? = null,

    @field:SerializedName("season")
    val season: String? = null,

    @field:SerializedName("year")
    val year: Int? = null
)

data class Anime(
    @field:SerializedName("mal_id")
    val malId: Int,

    @field:SerializedName("images")
    val images: Images,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("title_japanese")
    val titleJapanese: String,

    @field:SerializedName("type")
    val type: String,

    @field:SerializedName("source")
    val source: String,

    @field:SerializedName("episodes")
    val episodes: Int,

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("aired")
    val aired: Aired,

    @field:SerializedName("duration")
    val duration: String,

    @field:SerializedName("score")
    val score: Double? = null,

    @field:SerializedName("synopsis")
    val synopsis: String,

    @field:SerializedName("season")
    val season: String? = null,

    @field:SerializedName("year")
    val year: Int? = null,

    @field:SerializedName("studios")
    val studios: List<Studio>,

    @field:SerializedName("genres")
    val genres: List<Studio>
)

data class Images(
    @field:SerializedName("jpg")
    val jpg: Jpg
)

data class Jpg(
    @field:SerializedName("large_image_url")
    val largeImageUrl: String
)

data class Aired(
    @field:SerializedName("string")
    val string: String
)

data class Studio(
    @field:SerializedName("mal_id")
    val malId: Int,

    @field:SerializedName("name")
    val name: String
)