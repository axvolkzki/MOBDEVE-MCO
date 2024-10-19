package com.mobdeve.s14.amelia_delacruz.bookcol

import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s14.amelia_delacruz.bookcol.databinding.ItemBookFeedBinding

class FeedViewHolder(private val binding: ItemBookFeedBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(book: BookModel) {
        binding.bookTitleTextView.text = book.name
        binding.bookAuthorTextView.text = book.author
        binding.postedByTextView.text = book.username
        binding.bookImageView.setImageResource(book.cover)
    }
}
