package com.mobdeve.s14.abenoja_delacruz.bookcol.utils

import com.mobdeve.s14.abenoja_delacruz.bookcol.api.OpenLibraryApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://openlibrary.org/"

    val api: OpenLibraryApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenLibraryApiService::class.java)
    }
}