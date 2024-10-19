package com.mobdeve.s14.amelia_delacruz.bookcol

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.mobdeve.s14.amelia_delacruz.bookcol.databinding.ActivityShelfBinding

class ShelfActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityShelfBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewBinding = ActivityShelfBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Set up the RecyclerView
        viewBinding.rcvBooks.layoutManager = GridLayoutManager(this, 3)
        viewBinding.rcvBooks.adapter = MyAdapter(DataGenerator.loadData()) // Use MyAdapter here

        // Set up the click listener for the add button
        viewBinding.imgbtnCommunity.setOnClickListener {
            // Navigate to the FeedActivity
            goToFeedActivity()
        }
    }

    // Function to go to the FeedActivity
    private fun goToFeedActivity() {
        val intent = Intent(this, CommunityActivity::class.java)
        startActivity(intent)
        finish()
    }

    // Show the fragment for adding a book (if needed)
}
