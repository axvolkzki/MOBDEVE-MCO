package com.mobdeve.s14.amelia_delacruz.bookcol

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BookFeedAdapter(private val bookList: List<Book>) : RecyclerView.Adapter<BookFeedAdapter.BookViewHolder>() {

    // ViewHolder to hold the views for each book item
    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookImageView: ImageView = itemView.findViewById(R.id.bookImageView)
        val bookTitleTextView: TextView = itemView.findViewById(R.id.bookTitleTextView)
        val bookAuthorTextView: TextView = itemView.findViewById(R.id.bookAuthorTextView)
        val postedByTextView: TextView = itemView.findViewById(R.id.postedByTextView)
    }

    // Inflate the card view layout for each item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book_feed, parent, false)
        return BookViewHolder(view)
    }

    // Bind the book data to each view
    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = bookList[position]
        holder.bookTitleTextView.text = book.title
        holder.bookAuthorTextView.text = "by ${book.author}"
        holder.postedByTextView.text = "Posted by ${book.username}"

        // Dummy image (you can load real images using Glide/Picasso)
        holder.bookImageView.setImageResource(R.drawable.ic_book_placeholder)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }
}
