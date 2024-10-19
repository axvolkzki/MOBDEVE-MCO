package com.mobdeve.s14.amelia_delacruz.bookcol

class DataGenerator {
    companion object {
        fun loadData(): ArrayList<BookModel> {
            val data = ArrayList<BookModel>()
            data.add(
                BookModel(
                    "Book 1", R.mipmap.img1, "F. Scott Fitzgerald", "User1", true
                )
            )
            data.add(
                BookModel(
                    "Book 2", R.mipmap.img2, "George Orwell", "User2", false // Changed to false
                )
            )
            data.add(
                BookModel(
                    "Book 3", R.mipmap.img3, "Harper Lee", "User3", false // Changed to false
                )
            )
            data.add(
                BookModel(
                    "Book 4", R.mipmap.img4, "J.D. Salinger", "User4", true // Changed to true
                )
            )
            data.add(
                BookModel(
                    "Book 5", R.mipmap.img5, "Herman Melville", "User5", false // Changed to false
                )
            )
            return data
        }
    }
}
