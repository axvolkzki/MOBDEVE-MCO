package com.mobdeve.s14.abenoja_delacruz.bookcol

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s14.abenoja_delacruz.bookcol.databinding.ItemWishlistLayoutBinding

class TempWishAdapter(private val wishlist: ArrayList<WishlistModel>) : RecyclerView.Adapter<TempWishViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TempWishViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewBinding = ItemWishlistLayoutBinding.inflate(layoutInflater, parent, false)
        return TempWishViewHolder(viewBinding)
    }

    override fun getItemCount(): Int {
        return wishlist.size
    }

    override fun onBindViewHolder(holder: TempWishViewHolder, position: Int) {
        holder.bind(wishlist.get(position))
    }
}