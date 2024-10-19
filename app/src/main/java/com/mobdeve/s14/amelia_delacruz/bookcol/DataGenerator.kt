package com.mobdeve.s14.amelia_delacruz.bookcol

class DataGenerator {
    companion object {
        fun loadData(): ArrayList<BookModel> {
            val data = ArrayList<BookModel>()
            data.add(
                BookModel(
                    "Book 1", R.mipmap.img1
                )
            )
            data.add(
                BookModel(
                    "Book 2", R.mipmap.img2
                )
            )
            data.add(
                BookModel(
                    "Book 3", R.mipmap.img3
                )
            )
            data.add(
                BookModel(
                    "Book 4", R.mipmap.img4
                )
            )
            data.add(
                BookModel(
                    "Book 5", R.mipmap.img5
                )
            )
            return data
        }
    }
}
