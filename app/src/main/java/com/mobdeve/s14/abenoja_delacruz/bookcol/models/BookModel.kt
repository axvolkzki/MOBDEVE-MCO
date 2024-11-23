package com.mobdeve.s14.abenoja_delacruz.bookcol.models

import java.io.Serializable

data class BookModel(
    val bookID: String? = null,
    val title: String,
    val authors: List<Author>? = null,
    val publishDate: String? = null,
    val cover: Cover? = null,
    val isbn: String
) : Serializable // Add this line to make it serializable

data class Author(
    val name: String
) : Serializable // Add this line to make it serializable

data class Cover(
    val small: String? = null,
    val medium: String? = null,
    val large: String? = null
) : Serializable // Add this line to make it serializable


