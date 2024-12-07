package com.mobdeve.s14.abenoja_delacruz.bookcol.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.mobdeve.s14.abenoja_delacruz.bookcol.adapters.LibraryAdapter
import com.mobdeve.s14.abenoja_delacruz.bookcol.databinding.FragmentLibraryBinding


/**
 * A simple [Fragment] subclass.
 * Use the [LibraryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LibraryFragment : Fragment() {
    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rcvLibrary.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}