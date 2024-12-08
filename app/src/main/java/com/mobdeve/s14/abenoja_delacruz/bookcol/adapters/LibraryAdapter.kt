package com.mobdeve.s14.abenoja_delacruz.bookcol.adapters

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s14.abenoja_delacruz.bookcol.activities.BookDetailsActivity
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
        // Bind the book data to the view holder
        holder.bind(books[position])

        // Set an OnClickListener on the itemView
        holder.itemView.setOnClickListener {
            // Get the book title from the current position
            val title = books[position].title // Assuming 'title' is the property for book title

            // Create an intent to start BookDetailsActivity
            val intent = Intent(holder.itemView.context, BookDetailsActivity::class.java)
            intent.putExtra("KEY_TITLE", title) // Pass the book title
            holder.itemView.context.startActivity(intent) // Start the activity
        }
    }
}
