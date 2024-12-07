package com.mobdeve.s14.abenoja_delacruz.bookcol.models

import java.io.Serializable

data class BookResponseModel(
    val key: String?,   // e.g., "/books/OL12345M"
    val title: String?,
    val authors: List<Author>?,
    val covers: List<Int>?,  // Nullable list of integers for cover image IDs
    val publish_date: String?,
    val number_of_pages: Int?,
    val publishers: List<String>?,  // Changed from List<Publisher> to List<String>
    val isbn_13: List<String>?,
    val description: String?,
    val subjects: List<String>?,
) : Serializable

data class Author(
    val name: String?
) : Serializable

// No need for the Publisher class anymore



