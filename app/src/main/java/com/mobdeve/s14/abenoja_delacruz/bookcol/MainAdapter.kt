package com.mobdeve.s14.abenoja_delacruz.bookcol

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s14.abenoja_delacruz.bookcol.activities.BookDetailsActivity
import com.mobdeve.s14.abenoja_delacruz.bookcol.models.BookModel
import com.mobdeve.s14.abenoja_delacruz.bookcol.databinding.ItemBookLayoutBinding

class MainAdapter(private val books: ArrayList<BookModel>) : RecyclerView.Adapter<MainViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewBinding = ItemBookLayoutBinding.inflate(layoutInflater, parent, false)
        return MainViewHolder(viewBinding)
    }

    override fun getItemCount(): Int {
        return books.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(books.get(position))

        // When the user clicks on a book, the app will navigate to the BookDetailsActivity
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, BookDetailsActivity::class.java)
            intent.putExtra("KEY_COVER", books.get(position).cover)
            intent.putExtra("KEY_NAME", books.get(position).name)
            holder.itemView.context.startActivity(intent)
        }
    }
}