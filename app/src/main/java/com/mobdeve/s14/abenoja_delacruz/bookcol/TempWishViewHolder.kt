package com.mobdeve.s14.abenoja_delacruz.bookcol

import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s14.abenoja_delacruz.bookcol.databinding.ItemWishlistLayoutBinding

class TempWishViewHolder(private val viewBinding: ItemWishlistLayoutBinding) : RecyclerView.ViewHolder(viewBinding.root) {
    fun bind(wishlist: WishlistModel) {
        viewBinding.txvWishlistName.text = wishlist.name
        viewBinding.imgWishlistCover.setImageResource(wishlist.cover)
    }
}