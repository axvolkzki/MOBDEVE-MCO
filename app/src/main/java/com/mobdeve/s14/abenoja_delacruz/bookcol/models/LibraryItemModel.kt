package com.mobdeve.s14.abenoja_delacruz.bookcol.models

data class LibraryItemModel(
    val bookId: String, // Reference to Books collection
    val dateAdded: String, // Timestamp of when the book was added
)
