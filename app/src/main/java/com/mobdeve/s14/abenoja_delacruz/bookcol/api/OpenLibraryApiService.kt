package com.mobdeve.s14.abenoja_delacruz.bookcol.api

import com.mobdeve.s14.abenoja_delacruz.bookcol.models.BookModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface OpenLibraryApiService {
    @GET("isbn/{isbn}.json")
    fun getBookByISBN(@Path("isbn") isbn: String): Call<BookModel>
}