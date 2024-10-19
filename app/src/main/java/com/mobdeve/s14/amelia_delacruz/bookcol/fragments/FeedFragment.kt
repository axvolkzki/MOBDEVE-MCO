package com.mobdeve.s14.amelia_delacruz.bookcol.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s14.amelia_delacruz.bookcol.DataGenerator
import com.mobdeve.s14.amelia_delacruz.bookcol.FeedBookAdapter
import com.mobdeve.s14.amelia_delacruz.bookcol.MyAdapter
import com.mobdeve.s14.amelia_delacruz.bookcol.R


/**
 * A simple [Fragment] subclass.
 * Use the [FeedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FeedFragment : Fragment() {
    private lateinit var adapter: FeedBookAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_feed, container, false)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView)

        // Load data
        val data = DataGenerator.loadData() // Ensure this returns a List<BookModel>

        // Create the adapter
        adapter = FeedBookAdapter(data)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context) // Use a LinearLayoutManager
        recyclerView.adapter = adapter // Set the adapter to RecyclerView

        return view
    }
}
