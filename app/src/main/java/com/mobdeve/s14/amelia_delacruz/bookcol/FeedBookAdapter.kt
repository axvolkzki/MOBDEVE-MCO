package com.mobdeve.s14.amelia_delacruz.bookcol

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s14.amelia_delacruz.bookcol.databinding.FeedItemBookLayoutBinding

class FeedBookAdapter(private val data: List<BookModel>) : RecyclerView.Adapter<FeedBookAdapter.FeedBookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedBookViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewBinding = FeedItemBookLayoutBinding.inflate(layoutInflater, parent, false)
        return FeedBookViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: FeedBookViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class FeedBookViewHolder(private val viewBinding: FeedItemBookLayoutBinding) : RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(book: BookModel) {
            viewBinding.bookTitleTextView.text = book.name
            viewBinding.bookAuthorTextView.text = "by ${book.author}"
            viewBinding.postedByTextView.text = "Posted by ${book.postedBy}"
            viewBinding.bookImageView.setImageResource(book.cover)
        }
    }
}