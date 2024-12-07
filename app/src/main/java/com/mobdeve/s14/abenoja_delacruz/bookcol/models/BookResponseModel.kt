package com.mobdeve.s14.abenoja_delacruz.bookcol.models

import java.io.Serializable

data class BookResponseModel(
    val key: String?,   // e.g., "/books/OL12345M"
    val title: String?,
//    val authors: List<Map<String, String>>,
    val authors: List<Author>?,
    val cover: Map<String, String>?,
    val publishDate: String?,
    val pageCount: Int?,
    val publishers: List<Publisher>?,
    val isbn: List<String>,
) : Serializable

data class Author(
    val name: String?
) : Serializable

data class Publisher(
    val name: String?
) : Serializable
