package com.mobdeve.s14.abenoja_delacruz.bookcol.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.mobdeve.s14.abenoja_delacruz.bookcol.R

class ProfileFragment : Fragment() {

    private lateinit var profileImageView: ImageView
    private lateinit var userNameTextView: TextView
    private lateinit var userEmailTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Bind UI elements
        profileImageView = view.findViewById(R.id.profileImageView)
        userNameTextView = view.findViewById(R.id.usernameTextView)
        userEmailTextView = view.findViewById(R.id.emailTextView) // Assume you have an email TextView

        // Load user data from SharedPreferences
        loadUserData()

        return view
    }

    private fun loadUserData() {
        // Retrieve the saved user data from SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

        val userId = sharedPreferences.getString("userId", null)
        val username = sharedPreferences.getString("username", "Unknown User")
        val email = sharedPreferences.getString("email", "No email available")

        // Check if user is logged in
        if (userId != null) {
            // Set the user's profile details in the UI
            userNameTextView.text = username
            userEmailTextView.text = email

            // Set profile image (this can be customized later with actual image loading logic)
            profileImageView.setImageResource(R.drawable.ic_profile_placeholder) // Set a default or placeholder image
        } else {
            // Handle case where no user is logged in
            userNameTextView.text = "No user logged in"
            userEmailTextView.text = ""
        }
    }
}
