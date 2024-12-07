package com.mobdeve.s14.abenoja_delacruz.bookcol.activities

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.mobdeve.s14.abenoja_delacruz.bookcol.R
import com.mobdeve.s14.abenoja_delacruz.bookcol.databinding.ActivityScannedBookPreviewBinding
import com.mobdeve.s14.abenoja_delacruz.bookcol.models.BookResponseModel

class ScannedBookPreviewActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityScannedBookPreviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityScannedBookPreviewBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Get book details from intent
        val bookDetails = intent.getSerializableExtra("BOOK_DETAILS") as? BookResponseModel

        // Log book details to see if they're properly passed
        Log.d(TAG, "Received book details: $bookDetails")

        if (bookDetails != null) {
            // Set data to UI elements

            // Set cover image (if available)
            val coverUrl = bookDetails.covers?.firstOrNull()?.let { "https://covers.openlibrary.org/b/id/$it-M.jpg" }
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

            // Handle authors (if any) - If authors is null, use an empty list
            val authors = bookDetails.authors?.joinToString(", ") { it.name.orEmpty() } ?: "Unknown Authors"
            viewBinding.txvPrevAuthor.text = authors

            // Set publisher if available - If publishers is null, use an empty list
            val publishers = bookDetails.publishers?.joinToString(", ") ?: "Unknown Publisher"
            viewBinding.txvPrevPublisher.text = publishers

            // Set publish date if available
            viewBinding.txvPrevDatePublished.text = bookDetails.publish_date ?: "Unknown Publish Date"

            // Set ISBN (if available) - If isbn_13 is null, use an empty list
            viewBinding.txvPrevISBN.text = bookDetails.isbn_13?.joinToString(", ") { it } ?: "Unknown ISBN"

            // Set description if available
            viewBinding.txvPrevSummary.text = bookDetails.description ?: "No description available."

            // Set number of pages
            viewBinding.txvPrevPageNumber.text = bookDetails.number_of_pages?.toString() ?: "Unknown"

            // Set subjects if available
            viewBinding.txvPrevSubjects.text = bookDetails.subjects?.joinToString(", ") ?: "No subjects available."


        } else {
            Log.e(TAG, "No book details found in intent.")
        }



        // Setup back button functionality
        viewBinding.btnPrevBack.setOnClickListener {
            finish()
        }
    }
}
