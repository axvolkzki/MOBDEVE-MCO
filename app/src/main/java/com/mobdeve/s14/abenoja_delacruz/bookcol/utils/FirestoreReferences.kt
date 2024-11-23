package com.mobdeve.s14.abenoja_delacruz.bookcol.utils

import android.annotation.SuppressLint
import com.google.firebase.firestore.FirebaseFirestore

object FirestoreReferences {
    @SuppressLint("StaticFieldLeak")
    private val dbFirestore = FirebaseFirestore.getInstance()

    // Reference to the collections
    val usersCollection = dbFirestore.collection("Users")
    private val booksCollection = dbFirestore.collection("Books")

    // Helper to get user-specific books
    fun getUserBooks(userID: String) = booksCollection.whereEqualTo("userID", userID)

    // Helper to get a specific user
    fun getUser(userID: String) = usersCollection.document(userID)

    // Helper to get a specific book
    fun getBook(bookID: String) = booksCollection.document(bookID)

    fun userLibraryCollection(userId: String) = usersCollection.document(userId).collection("Library")
}