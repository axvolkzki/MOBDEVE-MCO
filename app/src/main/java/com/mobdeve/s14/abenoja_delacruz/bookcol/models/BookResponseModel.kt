package com.mobdeve.s14.abenoja_delacruz.bookcol.models

import java.io.Serializable

data class BookResponseModel(
    val key: String?,
    val title: String?,
    val authors: List<Author>?,
    val covers: List<Int>?,
    val publish_date: String?,
    val number_of_pages: Int?,
    val publishers: List<String>?,
    val isbn_13: List<String>?,
    val description: String?,
    val subjects: List<String>?
) : Serializable

data class Author(
    val key: String?   // The author's OpenLibrary key (e.g., "/authors/OL498120A")
) : Serializable

// This model will represent the author details from /authors/{authorKey}.json
data class AuthorDetails(
    val name: String?
) : Serializable




