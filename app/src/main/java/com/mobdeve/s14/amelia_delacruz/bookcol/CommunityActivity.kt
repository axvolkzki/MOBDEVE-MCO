package com.mobdeve.s14.amelia_delacruz.bookcol

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CommunityActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_community)
        // Get reference to RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        // Set a LayoutManager (LinearLayoutManager is typical for lists)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Create some dummy data
        val bookList = listOf(
            Book("The Great Gatsby", "F. Scott Fitzgerald", "User1"),
            Book("1984", "George Orwell", "User2"),
            Book("To Kill a Mockingbird", "Harper Lee", "User3"),
            Book("The Catcher in the Rye", "J.D. Salinger", "User4"),
            Book("Moby-Dick", "Herman Melville", "User5")
        )

        // Set the adapter
        val adapter = BookFeedAdapter(bookList)
        recyclerView.adapter = adapter
    }
}