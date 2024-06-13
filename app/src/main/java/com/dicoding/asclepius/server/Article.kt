package com.dicoding.skinSavvy.server

import com.google.gson.annotations.SerializedName

data class Article(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("urlToImage") val imageUrl: String
)