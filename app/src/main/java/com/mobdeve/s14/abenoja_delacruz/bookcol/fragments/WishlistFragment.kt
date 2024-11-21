package com.mobdeve.s14.abenoja_delacruz.bookcol.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.mobdeve.s14.abenoja_delacruz.bookcol.adapters.WishlistAdapter
import com.mobdeve.s14.abenoja_delacruz.bookcol.datagenerators.WishlistDataGenerator
import com.mobdeve.s14.abenoja_delacruz.bookcol.databinding.FragmentWishlistBinding


/**
 * A simple [Fragment] subclass.
 * Use the [WishlistFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WishlistFragment : Fragment() {
    private var _binding: FragmentWishlistBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWishlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rcvWishlists.layoutManager = GridLayoutManager(requireContext(), 2)

        val wishlistList = WishlistDataGenerator.loadWishlistData()

        binding.rcvWishlists.adapter = WishlistAdapter(wishlistList)

        // LOG THE WISHLIST DATA SIZE TO CHECK IF IT IS LOADED
        Log.d("WishlistFragment", "Wishlist Data Size: ${wishlistList.size}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}