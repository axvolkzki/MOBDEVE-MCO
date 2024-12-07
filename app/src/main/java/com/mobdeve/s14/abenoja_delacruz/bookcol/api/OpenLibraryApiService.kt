package com.mobdeve.s14.abenoja_delacruz.bookcol.api

import com.mobdeve.s14.abenoja_delacruz.bookcol.models.BookResponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface OpenLibraryApiService {
    // Fetch book details by ISBN
    @GET("/isbn/{isbn}.json")
    fun getBookByISBN(
        @Path("isbn") isbn: String
    ): Call<BookResponseModel>

    // Fetch book details by OLID
    @GET("/books/{olid}.json")
    fun getBookByOLID(
        @Path("olid") olid: String
    ): Call<BookResponseModel>
}