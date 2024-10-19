package com.mobdeve.s14.amelia_delacruz.bookcol.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.mobdeve.s14.amelia_delacruz.bookcol.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    private lateinit var profileImageView: ImageView
    private lateinit var userNameTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Bind UI elements
        profileImageView = view.findViewById(R.id.profileImageView)
        userNameTextView = view.findViewById(R.id.usernameTextView)

        // Load user data (this can be replaced with actual data)
        loadUserData()

        return view
    }

    private fun loadUserData() {
        // Here you would typically load user data from a database or API
        // For demonstration, we can set static values
        profileImageView.setImageResource(R.drawable.ic_profile_placeholder) // Set your profile image
        userNameTextView.text = "Username" // Set user's name
    }
}
