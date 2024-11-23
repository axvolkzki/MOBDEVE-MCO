package com.mobdeve.s14.abenoja_delacruz.bookcol.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s14.abenoja_delacruz.bookcol.models.BookModel
import com.mobdeve.s14.abenoja_delacruz.bookcol.databinding.ItemFeedBookLayoutBinding

class FeedBookViewHolder(private val viewBinding: ItemFeedBookLayoutBinding) : RecyclerView.ViewHolder(viewBinding.root) {
    fun bind(book: BookModel) {
        // Assuming BookModel has 'title', 'author', 'username', and 'cover' properties
        viewBinding.bookTitleTextView.text = book.name
        viewBinding.bookAuthorTextView.text = "by ${book.author}"
        viewBinding.postedByTextView.text = "Posted by ${book.postedBy}"
        viewBinding.bookImageView.setImageResource(book.cover) // Assuming 'cover' holds the drawable resource ID
    }
}
