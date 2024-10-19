package com.mobdeve.s14.amelia_delacruz.bookcol

class TempWishDataGenerator {
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