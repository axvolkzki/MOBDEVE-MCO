package com.mobdeve.s14.abenoja_delacruz.bookcol.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s14.abenoja_delacruz.bookcol.models.BookModel
import com.mobdeve.s14.abenoja_delacruz.bookcol.databinding.ItemBookLayoutBinding

class LibraryViewHolder(private val viewBinding: ItemBookLayoutBinding) : RecyclerView.ViewHolder(viewBinding.root) {
    fun bind(book: BookModel) {
        viewBinding.txvBookName.text = book.title
    }
}