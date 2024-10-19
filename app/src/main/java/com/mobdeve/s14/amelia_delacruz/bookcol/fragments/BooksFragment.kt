package com.mobdeve.s14.amelia_delacruz.bookcol.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.mobdeve.s14.amelia_delacruz.bookcol.DataGenerator
import com.mobdeve.s14.amelia_delacruz.bookcol.MyAdapter
import com.mobdeve.s14.amelia_delacruz.bookcol.databinding.FragmentBooksBinding


/**
 * A simple [Fragment] subclass.
 * Use the [BooksFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BooksFragment : Fragment() {
    private var _binding: FragmentBooksBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBooksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rcvBooks.layoutManager = GridLayoutManager(requireContext(), 3)

        val bookList = DataGenerator.loadData()
        println("Debug: Book list size = ${bookList.size}")  // Debug line

        val adapter = MyAdapter(bookList)
        binding.rcvBooks.adapter = adapter

        binding.rcvBooks.post {
            binding.rcvBooks.requestLayout()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}