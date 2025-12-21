package com.example.app_companion_miquel_julia

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ValorantApiService {

    @GET("val/content/v1/contents")
    fun getContent(
        @Query("api_key") apiKey: String, // Riot usa "api_key" como parámetro
        @Query("locale") locale: String = "es-ES" // Español
    ): Call<ValorantResponse>
}