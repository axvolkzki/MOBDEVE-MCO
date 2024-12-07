package com.mobdeve.s14.abenoja_delacruz.bookcol.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s14.abenoja_delacruz.bookcol.models.BookResponseModel
import com.mobdeve.s14.abenoja_delacruz.bookcol.databinding.ItemFeedBookLayoutBinding

class FeedBookViewHolder(private val viewBinding: ItemFeedBookLayoutBinding) : RecyclerView.ViewHolder(viewBinding.root) {
    fun bind(book: BookResponseModel) {
        viewBinding.bookTitleTextView.text = book.title
    }
}
