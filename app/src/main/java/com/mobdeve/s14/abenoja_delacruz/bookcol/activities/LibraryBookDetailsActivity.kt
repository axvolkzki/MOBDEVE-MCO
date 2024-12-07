package com.mobdeve.s14.abenoja_delacruz.bookcol.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.mobdeve.s14.abenoja_delacruz.bookcol.R
import com.mobdeve.s14.abenoja_delacruz.bookcol.databinding.ActivityLibraryBookDetailsBinding

class LibraryBookDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLibraryBookDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibraryBookDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the book details from the intent
        val title = intent.getStringExtra("KEY_TITLE")
        val author = intent.getStringExtra("KEY_AUTHOR")
        val publisher = intent.getStringExtra("KEY_PUBLISHER")
        val coverImage = intent.getStringExtra("KEY_COVER_IMAGE")
        val datePublished = intent.getStringExtra("KEY_DATE_PUBLISHED")
        val isbn = intent.getStringExtra("KEY_ISBN")
        val summary = intent.getStringExtra("KEY_SUMMARY")
        val pageNumber = intent.getIntExtra("KEY_PAGE_NUMBER", 0)
        val subjects = intent.getStringExtra("KEY_SUBJECTS")

        // Bind the book details to the layout
        binding.txvLibDeetsTitle.text = title
        binding.txvLibDeetsAuthor.text = author
        binding.txvLibDeetsPublisher.text = publisher
        binding.txvLibDeetsDatePublished.text = datePublished
        binding.txvLibDeetsISBN.text = isbn
        binding.txvLibDeetsSummary.text = summary
        binding.txvLibDeetsPageNumber.text = pageNumber.toString()
        binding.txvLibDeetsSubjects.text = subjects

        // Load the book cover image using Glide or Picasso
        if (!coverImage.isNullOrEmpty()) {
            val coverUrl = "https://covers.openlibrary.org/b/id/${coverImage?.get(0)}-L.jpg"
            Glide.with(this).load(coverUrl).into(binding.imgLibDeetsCover)
        }
        else {
            binding.imgLibDeetsCover.setImageResource(R.drawable.ic_book_placeholder)
        }

        // Back button logic
        binding.btnLibDeetsBack.setOnClickListener {
            finish()
        }

    }
}