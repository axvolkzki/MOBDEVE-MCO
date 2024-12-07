package com.mobdeve.s14.abenoja_delacruz.bookcol.adapters

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
        holder.bind(books[position])

        // When the user clicks on a book, the app will navigate to the BookDetailsActivity
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, BookDetailsActivity::class.java)
            intent.putExtra("KEY_TITLE", books[position].title)
            holder.itemView.context.startActivity(intent)
        }
    }
}
