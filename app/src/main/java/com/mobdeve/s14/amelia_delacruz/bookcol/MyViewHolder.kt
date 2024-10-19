package com.mobdeve.s14.amelia_delacruz.bookcol

import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s14.amelia_delacruz.bookcol.databinding.ItemBookLayoutBinding

class MyViewHolder(private val viewBinding: ItemBookLayoutBinding) : RecyclerView.ViewHolder(viewBinding.root) {
    fun bind(book: BookModel) {
        viewBinding.txvBookName.text = book.name
        viewBinding.imgBookCover.setImageResource(book.cover)
    }
}