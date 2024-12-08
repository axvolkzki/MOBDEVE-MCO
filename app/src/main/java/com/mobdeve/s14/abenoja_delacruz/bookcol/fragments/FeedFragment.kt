package com.mobdeve.s14.abenoja_delacruz.bookcol.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.mobdeve.s14.abenoja_delacruz.bookcol.adapters.FeedBookAdapter
import com.mobdeve.s14.abenoja_delacruz.bookcol.R
import com.mobdeve.s14.abenoja_delacruz.bookcol.activities.LibraryBookDetailsActivity
import com.mobdeve.s14.abenoja_delacruz.bookcol.models.BookResponseModel
import com.mobdeve.s14.abenoja_delacruz.bookcol.utils.FirestoreReferences


/**
 * A simple [Fragment] subclass.
 * Use the [FeedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FeedFragment : Fragment() {
    private lateinit var adapter: FeedBookAdapter
    private lateinit var recyclerView: RecyclerView
    private val firestore = FirebaseFirestore.getInstance()
    private val availableBooks = mutableListOf<BookResponseModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_feed, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)

        return view
    }


}
