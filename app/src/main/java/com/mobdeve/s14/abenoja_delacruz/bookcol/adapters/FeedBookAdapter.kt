package com.mobdeve.s14.abenoja_delacruz.bookcol.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s14.abenoja_delacruz.bookcol.models.BookModel
import com.mobdeve.s14.abenoja_delacruz.bookcol.databinding.ItemFeedBookLayoutBinding

class FeedBookAdapter(private val data: List<BookModel>) : RecyclerView.Adapter<FeedBookAdapter.FeedBookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedBookViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewBinding = ItemFeedBookLayoutBinding.inflate(layoutInflater, parent, false)
        return FeedBookViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: FeedBookViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class FeedBookViewHolder(private val viewBinding: ItemFeedBookLayoutBinding) : RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(book: BookModel) {
            viewBinding.bookTitleTextView.text = book.title
        }
    }
}
