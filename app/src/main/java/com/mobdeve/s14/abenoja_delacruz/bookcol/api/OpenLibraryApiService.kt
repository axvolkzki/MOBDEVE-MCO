package com.mobdeve.s14.abenoja_delacruz.bookcol.api

import com.mobdeve.s14.abenoja_delacruz.bookcol.models.BookModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenLibraryApiService {
    @GET("api/books")
    fun getBookByISBN(
        @Query("bibkeys") isbn: String,
        @Query("format") format: String = "json",
        @Query("jscmd") jscmd: String = "data"
    ): Call<BookModel>
}