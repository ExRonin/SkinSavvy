package com.dicoding.skinSavvy.server

import com.dicoding.skinSavvy.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val baseUrl = BuildConfig.BASE_URL
    fun create(): NewsApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(NewsApiService::class.java)
    }
}
