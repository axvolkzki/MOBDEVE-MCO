package com.mobdeve.s14.abenoja_delacruz.bookcol.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s14.abenoja_delacruz.bookcol.activities.LibraryBookDetailsActivity
import com.mobdeve.s14.abenoja_delacruz.bookcol.models.BookResponseModel
import com.mobdeve.s14.abenoja_delacruz.bookcol.databinding.ItemBookLayoutBinding
import com.mobdeve.s14.abenoja_delacruz.bookcol.viewholders.LibraryViewHolder

class LibraryAdapter(private val books: ArrayList<BookResponseModel>) : RecyclerView.Adapter<LibraryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewBinding = ItemBookLayoutBinding.inflate(layoutInflater, parent, false)
        return LibraryViewHolder(viewBinding)
    }

    override fun getItemCount(): Int {
        return books.size
    }

    override fun onBindViewHolder(holder: LibraryViewHolder, position: Int) {
        val book = books[position] // Get the book at the current position
        holder.bind(book)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, LibraryBookDetailsActivity::class.java)

            Log.e("LibraryAdapter", "Book: $book")

            // Add book details to the intent
            intent.putExtra("KEY_TITLE", book.title)
            intent.putExtra("KEY_AUTHOR", book.authors?.joinToString(", ") { it.key ?: "Unknown Author" })
            intent.putExtra("KEY_PUBLISHER", book.publishers?.joinToString(", ") ?: "Unknown Publisher")

            // Handle empty or null covers
            val coverId = if (!book.covers.isNullOrEmpty()) book.covers[0] else null
            coverId?.let { intent.putExtra("KEY_COVER", it) }

            intent.putExtra("KEY_DATE_PUBLISHED", book.publish_date ?: "Unknown Date")
            intent.putExtra("KEY_ISBN", book.isbn_13?.joinToString(", ") ?: "Unknown ISBN")
            intent.putExtra("KEY_SUMMARY", book.description ?: "No summary available")
            intent.putExtra("KEY_PAGE_NUMBER", book.number_of_pages)
            intent.putExtra("KEY_SUBJECTS", book.subjects?.joinToString(", ") ?: "No subjects available")

            // Start the details activity
            holder.itemView.context.startActivity(intent)
        }
    }
}
