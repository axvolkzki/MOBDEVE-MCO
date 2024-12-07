package com.mobdeve.s14.abenoja_delacruz.bookcol.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.mobdeve.s14.abenoja_delacruz.bookcol.MainActivity
import com.mobdeve.s14.abenoja_delacruz.bookcol.R
import com.mobdeve.s14.abenoja_delacruz.bookcol.databinding.FragmentProfileBinding
import com.mobdeve.s14.abenoja_delacruz.bookcol.utils.SessionManager

class ProfileFragment : Fragment() {

    private lateinit var _binding: FragmentProfileBinding
    private val binding get() = _binding!!

    private lateinit var sessionManager: SessionManager
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionManager = SessionManager(requireContext())
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root


        // Load user data from SharedPreferences
        loadUserData()

        // Handle logout button
        _binding.btnProfileLogout.setOnClickListener {
            handleLogout()
        }

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
            _binding.usernameTextView.text = username
            _binding.emailTextView.text = email

            // Set profile image (this can be customized later with actual image loading logic)
            _binding.profileImageView.setImageResource(R.drawable.ic_profile_placeholder) // Set a default or placeholder image
        } else {
            // Handle case where no user is logged in
            _binding.usernameTextView.text = "Unknown User"
            _binding.emailTextView.text = "No email available"
        }
    }

    private fun handleLogout() {
        // Sign out the user
        auth.signOut()

        // Clear user data from SharedPreferences
        sessionManager.clearSession()

        // Return to MainActivity
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }
}
