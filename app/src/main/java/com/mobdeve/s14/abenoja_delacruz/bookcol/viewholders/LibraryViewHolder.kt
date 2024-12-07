package com.mobdeve.s14.abenoja_delacruz.bookcol.viewholders

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobdeve.s14.abenoja_delacruz.bookcol.R
import com.mobdeve.s14.abenoja_delacruz.bookcol.models.BookResponseModel
import com.mobdeve.s14.abenoja_delacruz.bookcol.databinding.ItemBookLayoutBinding


class LibraryViewHolder(private val viewBinding: ItemBookLayoutBinding) : RecyclerView.ViewHolder(viewBinding.root) {
    fun bind(book: BookResponseModel) {
        viewBinding.txvBookName.text = book.title

        // Check if book.covers is not empty or null
        val coverUrl = book.covers?.firstOrNull()?.let {
            "https://covers.openlibrary.org/b/id/$it-M.jpg"
        }

        if (!coverUrl.isNullOrEmpty()) {
            // Load the book cover using Glide
            Glide.with(viewBinding.root.context)
                .load(coverUrl)
                .placeholder(R.drawable.bg_login_background) // Placeholder while loading
                .error(R.drawable.bg_login_background) // Image if error loading
                .into(viewBinding.imgBookCover)
        } else {
            // If no cover, set a default image
            viewBinding.imgBookCover.setImageResource(R.drawable.bg_login_background)
        }

        // Log cover URL for debugging
        Log.d("LibraryViewHolder", "Cover URL: $coverUrl")
    }
}
