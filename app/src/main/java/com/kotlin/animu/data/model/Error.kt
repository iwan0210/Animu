package com.kotlin.animu.data.model

import com.google.gson.annotations.SerializedName

data class Error(
    @field:SerializedName("status")
    val status: Int,

    @field:SerializedName("type")
    val type: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("error")
    val error: String
)
