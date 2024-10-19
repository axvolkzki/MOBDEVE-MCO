package com.mobdeve.s14.amelia_delacruz.bookcol

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s14.amelia_delacruz.bookcol.databinding.ActivityBookDetailsBinding

class BookDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cover = intent.getIntExtra("KEY_COVER", 0)
        val name = intent.getStringExtra("KEY_NAME")

        binding.imgCover.setImageResource(cover)
        binding.txvTitle.text = name
    }
}