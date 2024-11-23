package com.mobdeve.s14.abenoja_delacruz.bookcol.datagenerators

import com.mobdeve.s14.abenoja_delacruz.bookcol.R
import com.mobdeve.s14.abenoja_delacruz.bookcol.models.BookModel


class DataGenerator {
    companion object {
        fun loadData(): ArrayList<BookModel> {
            val data = ArrayList<BookModel>()
            data.add(
                BookModel("Book 1", R.mipmap.img1, "F. Scott Fitzgerald", "User1")
            )
            data.add(
                BookModel("Book 2", R.mipmap.img2, "F. Scott Fitzgerald", "User2")
            )
            data.add(
                BookModel("Book 3", R.mipmap.img3, "F. Scott Fitzgerald", "User3")
            )
            data.add(
                BookModel("Book 4", R.mipmap.img4, "F. Scott Fitzgerald", "User4")
            )
            data.add(
                BookModel("Book 5", R.mipmap.img5, "F. Scott Fitzgerald", "User5")
            )
            return data
        }
    }
}
