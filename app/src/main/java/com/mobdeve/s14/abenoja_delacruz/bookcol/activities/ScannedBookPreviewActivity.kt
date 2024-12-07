package com.mobdeve.s14.abenoja_delacruz.bookcol.activities

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import com.mobdeve.s14.abenoja_delacruz.bookcol.R
import com.mobdeve.s14.abenoja_delacruz.bookcol.databinding.ActivityScannedBookPreviewBinding
import com.mobdeve.s14.abenoja_delacruz.bookcol.models.Author
import com.mobdeve.s14.abenoja_delacruz.bookcol.models.BookResponseModel
import com.mobdeve.s14.abenoja_delacruz.bookcol.utils.FirestoreReferences

class ScannedBookPreviewActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityScannedBookPreviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityScannedBookPreviewBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Get book details from intent
        val bookDetails = intent.getSerializableExtra("BOOK_DETAILS") as? BookResponseModel
        val authorNames: List<String>? = intent.getStringArrayListExtra("AUTHOR_NAMES")


        // Log book details to see if they're properly passed
        Log.d(TAG, "Received book details: $bookDetails")

        if (bookDetails != null) {
            // Set data to UI elements

            // Set cover image (if available)
//            val coverUrl = bookDetails.covers?.firstOrNull()?.let { "https://covers.openlibrary.org/b/id/$it-M.jpg" }
//            if (!coverUrl.isNullOrEmpty()) {
//                Glide.with(this)
//                    .load(coverUrl)
//                    .into(viewBinding.imgPrevCover)
//            } else {
//                // Provide a default image if no cover is available
//                viewBinding.imgPrevCover.setImageResource(R.drawable.bg_login_background)
//            }
            val coverUrl = when (val covers = bookDetails?.covers) {
                is List<*> -> covers.firstOrNull()?.toString()?.let { "https://covers.openlibrary.org/b/id/$it-M.jpg" }
                else -> {
                    // Try to safely convert to Long if it's a number
                    try {
                        val coverId = covers?.toString()?.toLongOrNull()
                        coverId?.let { "https://covers.openlibrary.org/b/id/$it-M.jpg" }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error processing cover ID: ${e.message}")
                        null
                    }
                }
            }

            if (!coverUrl.isNullOrEmpty()) {
                Glide.with(this)
                    .load(coverUrl)
                    .into(viewBinding.imgPrevCover)
            } else {
                // Provide a default image if no cover is available
                viewBinding.imgPrevCover.setImageResource(R.drawable.bg_login_background)
            }


            // Set title
            viewBinding.txvPrevTitle.text = bookDetails.title ?: "Unknown Title"

            // Handle authors - Display the passed author's name
            var authorNameList = ""
            if (authorNames != null) {
                authorNameList = authorNames.joinToString(", ")
            } else {
                authorNameList = "Unknown Author"
            }
            viewBinding.txvPrevAuthor.text = authorNameList



            // Set publisher if available - If publishers is null, use an empty list
            val publishers = bookDetails.publishers?.joinToString(", ") ?: "Unknown Publisher"
            viewBinding.txvPrevPublisher.text = publishers

            // Set publish date if available
            viewBinding.txvPrevDatePublished.text = bookDetails.publish_date ?: "Unknown Publish Date"

            // Set ISBN (if available) - If isbn_13 is null, use an empty list
//            viewBinding.txvPrevISBN.text = bookDetails.isbn_13?.joinToString(", ") { it } ?: "Unknown ISBN"
//            val isbn13s = bookDetails?.isbn_13?.joinToString(", ") ?: "Unknown ISBN"
//            viewBinding.txvPrevISBN.text = isbn13s
            val isbn13s = bookDetails?.isbn_13?.takeIf { it.isNotEmpty() }?.joinToString(", ") ?: "Unknown ISBN"
            viewBinding.txvPrevISBN.text = isbn13s

            // Set description if available
            viewBinding.txvPrevSummary.text = bookDetails.description ?: "No description available."

            // Set number of pages
            viewBinding.txvPrevPageNumber.text = bookDetails.number_of_pages?.toString() ?: "Unknown"

            // Set subjects if available
//            viewBinding.txvPrevSubjects.text = bookDetails.subjects?.joinToString(", ") ?: "No subjects available."
            val subjects = bookDetails.subjects?.joinToString(", ") ?: "No subjects available."
            viewBinding.txvPrevSubjects.text = subjects

        } else {
            Log.e(TAG, "No book details found in intent.")
        }

        // Setup back button functionality
        viewBinding.btnPrevBack.setOnClickListener {
            finish()
        }

        // Setup add to library button functionality
        viewBinding.btnPrevAddToLibrary.setOnClickListener {
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

            // Create the selected book, passing the actual author's name
            val selectedBook = BookResponseModel(
                key = bookDetails?.key,
                title = bookDetails?.title,
                authors = bookDetails?.authors, // Map author keys to Author objects
                covers = bookDetails?.covers,
                publish_date = bookDetails?.publish_date,
                number_of_pages = bookDetails?.number_of_pages,
                publishers = bookDetails?.publishers,
                isbn_13 = bookDetails?.isbn_13,
                description = bookDetails?.description,
                subjects = bookDetails?.subjects
            )

            // Pass the author name separately in a different field
            addBookToLibrary(userId, selectedBook, authorNames?.firstOrNull())

            // Go back to the Library Fragment after adding the book
            finish()
        }

        // Setup add to wishlist button functionality
        viewBinding.btnPrevAddToWishlist.setOnClickListener {
            // Add book to user's wishlist

            // Book details should be passed to the next activity

        }
    }

    private fun addBookToLibrary(userId: String, book: BookResponseModel, authorName: String?) {
        val firestore = FirebaseFirestore.getInstance()

        if (userId.isNotEmpty()) {
            // Create a new document with an auto-generated bookId
            val newBookRef = firestore.collection(FirestoreReferences.BOOK_COLLECTION).document()

            // Generate a new unique bookId for the new book entry
            val bookId = newBookRef.id

            // Set the book data (including the generated bookId) for the new book entry
            val bookData = hashMapOf(
                FirestoreReferences.BOOKID_FIELD to bookId,
                FirestoreReferences.COVERS_FIELD to (book.covers ?: listOf<Long>()), // Ensure covers is always saved as a List<Long>
                FirestoreReferences.TITLE_FIELD to (book.title ?: ""),
                //FirestoreReferences.AUTHORS_FIELD to (authorName ?: ""), // Use the actual author name
                FirestoreReferences.AUTHORS_FIELD to (book.authors?.map { Author(it.key) } ?: listOf()), // Save as List<Author>
                FirestoreReferences.PUBLISHERS_FIELD to (book.publishers ?: listOf("")), // Save as List<String>
                FirestoreReferences.PUBLISH_DATE_FIELD to (book.publish_date ?: ""),
                FirestoreReferences.ISBN_13_FIELD to (book.isbn_13 ?: listOf("")), // Save as List<String>
                FirestoreReferences.DESCRIPTION_FIELD to (book.description ?: ""),
                FirestoreReferences.NUMBER_OF_PAGES_FIELD to (book.number_of_pages ?: 0),
                FirestoreReferences.SUBJECTS_FIELD to (book.subjects ?: listOf("")) // Save as List<String>

            )

            // Set the generated book data in Firestore
            newBookRef.set(bookData)
                .addOnSuccessListener {
                    // Successfully added book details
                    Log.d(TAG, "Book added with ID: $bookId")

                    // Now, create the library entry linking the user and the book
                    val libraryEntry = hashMapOf(
                        "userId" to userId,
                        "bookId" to bookId,
                        "dateAdded" to com.google.firebase.firestore.FieldValue.serverTimestamp()
                    )

                    // Add the library entry to the Libraries collection
                    firestore.collection(FirestoreReferences.LIBRARY_COLLECTION)
                        .add(libraryEntry)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "Book added to user's library with entry ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { exception ->
                            Log.e(TAG, "Error adding book to library: ${exception.message}")
                        }
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error adding book details: ${exception.message}")
                }
        } else {
            Log.e(TAG, "Invalid user ID.")
        }
    }

}

