package com.mobdeve.s14.amelia_delacruz.bookcol

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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

        // Load data using DataGenerator
        val bookList = DataGenerator.loadData()

        // Set the adapter with the loaded data
        val adapter = MyAdapter(bookList) // Use MyAdapter
        recyclerView.adapter = adapter // Set the adapter to the RecyclerView
    }
}
