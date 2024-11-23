package com.mobdeve.s14.abenoja_delacruz.bookcol.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s14.abenoja_delacruz.bookcol.viewholders.WishlistViewHolder
import com.mobdeve.s14.abenoja_delacruz.bookcol.databinding.ItemWishlistLayoutBinding
import com.mobdeve.s14.abenoja_delacruz.bookcol.models.WishlistModel

class WishlistAdapter(private val wishlist: ArrayList<WishlistModel>) : RecyclerView.Adapter<WishlistViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishlistViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewBinding = ItemWishlistLayoutBinding.inflate(layoutInflater, parent, false)
        return WishlistViewHolder(viewBinding)
    }

    override fun getItemCount(): Int {
        return wishlist.size
    }

    override fun onBindViewHolder(holder: WishlistViewHolder, position: Int) {
        holder.bind(wishlist.get(position))
    }
}