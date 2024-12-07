package com.mobdeve.s14.abenoja_delacruz.bookcol.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s14.abenoja_delacruz.bookcol.activities.BookDetailsActivity
import com.mobdeve.s14.abenoja_delacruz.bookcol.viewholders.WishlistViewHolder
import com.mobdeve.s14.abenoja_delacruz.bookcol.databinding.ItemWishlistLayoutBinding
import com.mobdeve.s14.abenoja_delacruz.bookcol.models.BookResponseModel

class WishlistAdapter(private val books: ArrayList<BookResponseModel>) : RecyclerView.Adapter<WishlistViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishlistViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewBinding = ItemWishlistLayoutBinding.inflate(layoutInflater, parent, false)
        return WishlistViewHolder(viewBinding)
    }

    override fun getItemCount(): Int {
        return books.size
    }

    override fun onBindViewHolder(holder: WishlistViewHolder, position: Int) {
        holder.bind(books[position])

        // When the user clicks on a book, the app will navigate to the BookDetailsActivity
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, BookDetailsActivity::class.java)
            intent.putExtra("KEY_TITLE", books[position].title)
            holder.itemView.context.startActivity(intent)
        }
    }
}