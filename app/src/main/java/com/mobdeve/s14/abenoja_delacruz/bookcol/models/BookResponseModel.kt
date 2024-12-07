package com.mobdeve.s14.abenoja_delacruz.bookcol.models

import java.io.Serializable

data class BookResponseModel(
    val key: String? = null,
    val title: String? = null,
    val authors: List<Author>? = null,
    val covers: List<Long>? = null,
    val publish_date: String? = null,
    val number_of_pages: Int? = null,
    val publishers: List<String>? = null,  // Ensure this is a List<String> to match Firestore data
    val isbn_13: List<String>? = null,
    val description: String? = null,
    val subjects: List<String>? = null,
) : Serializable

data class Author(
    val key: String? = null   // The author's OpenLibrary key (e.g., "/authors/OL498120A")
) : Serializable

// This model will represent the author details from /authors/{authorKey}.json
data class AuthorDetails(
    val name: String? = null
) : Serializable




