package com.mobdeve.s14.abenoja_delacruz.bookcol.datagenerators

import com.mobdeve.s14.abenoja_delacruz.bookcol.R
import com.mobdeve.s14.abenoja_delacruz.bookcol.models.WishlistModel


class WishlistDataGenerator {
    companion object {
        fun loadWishlistData(): ArrayList<WishlistModel> {
            val data = ArrayList<WishlistModel>()
            data.add(
                WishlistModel(
                    "Wish 1", R.mipmap.wish1
                )
            )
            data.add(
                WishlistModel(
                    "Wish 2", R.mipmap.wish2
                )
            )
            data.add(
                WishlistModel(
                    "Wish 3", R.mipmap.wish3
                )
            )
            data.add(
                WishlistModel(
                    "Wish 4", R.mipmap.wish4
                )
            )
            data.add(
                WishlistModel(
                    "Wish 5", R.mipmap.wish5
                )
            )
            return data
        }
    }
}