package com.mobdeve.s14.abenoja_delacruz.bookcol.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mobdeve.s14.abenoja_delacruz.bookcol.R
import com.mobdeve.s14.abenoja_delacruz.bookcol.databinding.ActivityLibraryBookDetailsBinding
import com.mobdeve.s14.abenoja_delacruz.bookcol.utils.FirestoreReferences

class LibraryBookDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLibraryBookDetailsBinding
    private val firestore = FirebaseFirestore.getInstance()

    // Add bookId variable to store the current book's ID
    private var bookId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibraryBookDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the key_title from the intent
        val keyTitle = intent.getStringExtra("KEY_TITLE")

        if (!keyTitle.isNullOrEmpty()) {
            fetchBookDetails(keyTitle)
        } else {
            Toast.makeText(this, "No title provided", Toast.LENGTH_SHORT).show()
        }

        // Set the back button to finish the activity
        binding.btnPrevBack.setOnClickListener {
            finish()
        }

        // Lend button
        binding.btnLibDeetsLendBook.setOnClickListener {
            // Implement lending functionality here
            handleLendButton()
        }

    }

    private fun fetchBookDetails(title: String) {
        firestore.collection("Books")
            .whereEqualTo("title", title)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val book = querySnapshot.documents[0] // Assuming title is unique
                    bookId = book.id
                    displayBookDetails(book.data)
                    updateLendButtonState()
                } else {
                    Toast.makeText(this, "Book not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Log.e("BookDetailsActivity", "Error fetching book details", e)
                Toast.makeText(this, "Error fetching book details", Toast.LENGTH_SHORT).show()
            }
    }

    private fun displayBookDetails(bookData: Map<String, Any>?) {
        if (bookData != null) {
            binding.txvPrevTitle.text = bookData["title"] as? String ?: "N/A"
            binding.txvPrevAuthor.text = (bookData["authors"] as? List<*>)?.joinToString(", ") ?: "N/A"
            binding.txvPrevSummary.text = bookData["desciption"] as? String ?: "No description available"
            binding.txvPrevDatePublished.text = bookData["publish_date"] as? String ?: "N/A"
            binding.txvPrevPublisher.text = (bookData["publishers"] as? List<*>)?.joinToString(", ") ?: "N/A"
            binding.txvPrevISBN.text = (bookData["isbn_13"] as? List<*>)?.joinToString(", ") ?: "N/A"
            binding.txvPrevPageNumber.text = (bookData["number_of_pages"] as? Number)?.toString() ?: "N/A"

            // Handle the book cover
            val coverUrl = getCoverUrl(bookData["covers"])
            coverUrl?.let {
                Glide.with(this)
                    .load(it)
                    .placeholder(R.drawable.bg_login_background) // Placeholder image shown while loading
                    .error(R.drawable.bg_login_background)      // Error image shown if loading fails
                    .into(binding.imgPrevCover)
            } ?: run {
                // If coverUrl is null, show a placeholder image
                Glide.with(this)
                    .load(R.drawable.bg_login_background)
                    .into(binding.imgPrevCover)
            }
        } else {
            Toast.makeText(this, "No book data to display", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCoverUrl(covers: Any?): String? {
        return when (covers) {
            is List<*> -> covers.firstOrNull()?.toString()?.let { "https://covers.openlibrary.org/b/id/$it-M.jpg" }
            else -> {
                try {
                    val coverId = covers?.toString()?.toLongOrNull()
                    coverId?.let { "https://covers.openlibrary.org/b/id/$it-M.jpg" }
                } catch (e: Exception) {
                    Log.e("BookDetailsActivity", "Error processing cover ID: ${e.message}")
                    null
                }
            }
        }
    }

    private fun handleLendButton() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null || bookId == null) {
            Toast.makeText(this, "Unable to process request", Toast.LENGTH_SHORT).show()
            return
        }

        firestore.collection(FirestoreReferences.LIBRARY_COLLECTION)
            .whereEqualTo(FirestoreReferences.LIBRARY_USERID_FIELD, userId)
            .whereEqualTo(FirestoreReferences.LIBRARY_BOOKID_FIELD, bookId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val libraryDoc = querySnapshot.documents[0]
                    val isCurrentlyForLending = libraryDoc.getBoolean(FirestoreReferences.LIBRARY_IS_FOR_LENDING) ?: false

                    // Toggle the lending status
                    libraryDoc.reference.update(FirestoreReferences.LIBRARY_IS_FOR_LENDING, !isCurrentlyForLending)
                        .addOnSuccessListener {
                            val statusMessage = if (!isCurrentlyForLending)
                                "Book is now available for lending"
                            else
                                "Book is no longer available for lending"
                            Toast.makeText(this, statusMessage, Toast.LENGTH_SHORT).show()
                            updateLendButtonState(!isCurrentlyForLending)
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error updating lending status", Toast.LENGTH_SHORT).show()
                            Log.e("LibraryBookDetails", "Error updating lending status", e)
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error checking library status", Toast.LENGTH_SHORT).show()
                Log.e("LibraryBookDetails", "Error checking library status", e)
            }
    }

    private fun updateLendButtonState(isForLending: Boolean? = null) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null || bookId == null) return

        if (isForLending != null) {
            // Update UI directly if we know the state
            binding.btnLibDeetsLendBook.text = if (isForLending) "Stop Lending" else "Lend Book"
            return
        }

        // Query Firestore for the current state
        firestore.collection(FirestoreReferences.LIBRARY_COLLECTION)
            .whereEqualTo(FirestoreReferences.LIBRARY_USERID_FIELD, userId)
            .whereEqualTo(FirestoreReferences.LIBRARY_BOOKID_FIELD, bookId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val isForLending = querySnapshot.documents[0].getBoolean(FirestoreReferences.LIBRARY_IS_FOR_LENDING) ?: false
                    binding.btnLibDeetsLendBook.text = if (isForLending) "Stop Lending" else "Lend Book"
                }
            }
    }
}