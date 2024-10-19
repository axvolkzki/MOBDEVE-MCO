package com.mobdeve.s14.amelia_delacruz.bookcol

import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s14.amelia_delacruz.bookcol.databinding.ItemBookLayoutBinding

class LayoutViewHolder(private val binding: ItemBookLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(book: BookModel) {
        binding.txvBookName.text = book.name
        binding.imgBookCover.setImageResource(book.cover)
    }
}