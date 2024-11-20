package com.mobdeve.s14.abenoja_delacruz.bookcol

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s14.abenoja_delacruz.bookcol.databinding.ItemBookLayoutBinding

class MyAdapter(private val books: ArrayList<BookModel>) : RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewBinding = ItemBookLayoutBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(viewBinding)
    }

    override fun getItemCount(): Int {
        return books.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
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