package com.mobdeve.s14.abenoja_delacruz.bookcol.api

import com.mobdeve.s14.abenoja_delacruz.bookcol.models.BookResponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface OpenLibraryApiService {
    // Resolves ISBN to book metadata
    @GET("isbn/{isbn}.json")
    fun getBookByISBN(@Path("isbn") isbn: String): Call<Map<String, Any>>

    // Fetches book details by OLID
    @GET("books/{olid}.json")
    fun getBookByOLID(@Path("olid") olid: String): Call<BookResponseModel>
}