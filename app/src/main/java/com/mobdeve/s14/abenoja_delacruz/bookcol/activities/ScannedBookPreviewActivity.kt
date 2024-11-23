package com.mobdeve.s14.abenoja_delacruz.bookcol.activities

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.mobdeve.s14.abenoja_delacruz.bookcol.R
import com.mobdeve.s14.abenoja_delacruz.bookcol.databinding.ActivityScannedBookPreviewBinding
import com.mobdeve.s14.abenoja_delacruz.bookcol.models.BookModel

class ScannedBookPreviewActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityScannedBookPreviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityScannedBookPreviewBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Get book details from intent
        val bookDetails = intent.getSerializableExtra("BOOK_DETAILS") as? BookModel

        if (bookDetails != null) {
            // Set data to UI elements
            viewBinding.txvPrevTitle.text = bookDetails.title
            viewBinding.txvPrevISBN.text = bookDetails.isbn

            // Handle authors (if any)
            val authors = bookDetails.authors?.joinToString { it.name } ?: "Unknown"
            viewBinding.txvPrevAuthor.text = authors

            // Set publisher if available
            viewBinding.txvPrevPublisher.text = bookDetails.publishDate ?: "Unknown"

            // Set cover image (if available)
            val coverUrl = bookDetails.cover?.medium
            if (!coverUrl.isNullOrEmpty()) {
                Glide.with(this)
                    .load(coverUrl)
                    .into(viewBinding.imgPrevCover)
            } else {
                // Provide a default image if no cover is available
                viewBinding.imgPrevCover.setImageResource(R.drawable.bg_login_background)
            }
        } else {
            Log.e(TAG, "No book details found in intent.")
        }

        // Setup back button functionality
        viewBinding.btnPrevBack.setOnClickListener {
            finish()
        }
    }
}
