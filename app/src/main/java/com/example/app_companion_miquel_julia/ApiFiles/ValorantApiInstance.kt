package com.example.app_companion_miquel_julia.ApiFiles

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ValorantApiInstance {

    private const val BASE_URL = "https://eu.api.riotgames.com/"

    val apiService: ValorantApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ValorantApiService::class.java)
    }
}