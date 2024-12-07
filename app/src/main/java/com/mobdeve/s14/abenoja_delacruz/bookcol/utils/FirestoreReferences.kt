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
    const val COVERS_FIELD = "covers"
    const val TITLE_FIELD = "title"
    const val AUTHORS_FIELD = "authors"
    const val PUBLISHERS_FIELD = "publishers"
    const val PUBLISH_DATE_FIELD = "publish_date"
    const val ISBN_13_FIELD = "isbn_13"
    const val DESCRIPTION_FIELD = "desciption"
    const val NUMBER_OF_PAGES_FIELD = "number_of_pages"
    const val LANGUAGE_FIELD = "language"
    const val SUBJECTS_FIELD = "subjects"

    // Library fields
    const val LIBRARY_USERID_FIELD = "userId"
    const val LIBRARY_BOOKID_FIELD = "bookId"
    const val DATE_ADDED_FIELD = "dateAdded"
}