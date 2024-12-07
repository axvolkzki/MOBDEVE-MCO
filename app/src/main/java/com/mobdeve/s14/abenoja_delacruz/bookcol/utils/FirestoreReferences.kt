package com.mobdeve.s14.abenoja_delacruz.bookcol.utils


object FirestoreReferences {
    // Collection references
    const val USER_COLLECTION = "Users"
    const val BOOK_COLLECTION = "Books"
    const val LIBRARY_COLLECTION = "Libraries"

    // User fields
    const val USERID_FIELD = "userId"
    const val USERNAME_FIELD = "username"
    const val EMAIL_FIELD = "email"

    // Book fields
    const val BOOKID_FIELD = "bookId"
    const val COVER_FIELD = "cover"
    const val TITLE_FIELD = "title"
    const val AUTHOR_FIELD = "author"
    const val PUBLISHER_FIELD = "publisher"
    const val PUBLISH_DATE_FIELD = "publishDate"
    const val ISBN_FIELD = "isbn"
    const val SUMMARY_FIELD = "summary"
    const val PAGE_COUNT_FIELD = "pageCount"
    const val LANGUAGE_FIELD = "language"
    const val GENRE_FIELD = "genre"

    // Library fields
    const val LIBRARY_USERID_FIELD = "userId"
    const val LIBRARY_BOOKID_FIELD = "bookId"
    const val DATE_ADDED_FIELD = "dateAdded"
}