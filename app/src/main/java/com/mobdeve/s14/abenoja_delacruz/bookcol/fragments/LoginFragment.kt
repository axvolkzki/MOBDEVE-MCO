package com.mobdeve.s14.abenoja_delacruz.bookcol.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mobdeve.s14.abenoja_delacruz.bookcol.R
import com.mobdeve.s14.abenoja_delacruz.bookcol.activities.BaseActivity
import com.mobdeve.s14.abenoja_delacruz.bookcol.databinding.FragmentLoginBinding
import com.mobdeve.s14.abenoja_delacruz.bookcol.utils.toast

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    // Use ViewBinding
    private var _binding: FragmentLoginBinding? = null
    private val viewBinding get() = _binding!!

    private lateinit var mAuth: FirebaseAuth
    private lateinit var dbFirestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()
        dbFirestore = FirebaseFirestore.getInstance()

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize viewBinding
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle login button click
        viewBinding.btnLoginSubmit.setOnClickListener {
            handleLogIn()
        }

        // Navigate to SignupFragment
        viewBinding.btnLoginToSignUp.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.slide_out, R.anim.slide_in, R.anim.slide_out)
                .replace(R.id.flWrapper, SignupFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun handleLogIn() {
        val username = viewBinding.etLoginUsername.text.toString().trim()
        val password = viewBinding.etLoginPassword.text.toString().trim()

        // Validate input
        if (username.isBlank() || password.isBlank()) {
            validateInput(username, password)
            return
        }

        if (password.length < 6) {
            viewBinding.etLoginPassword.error = "Password must be at least 6 characters"
            return
        }

        // Perform login
        performLogin(username, password)
    }

    private fun validateInput(username: String, password: String): Boolean {
        return when {
            username.isEmpty() -> {
                viewBinding.etLoginUsername.error = "Username is required"
                false
            }
            password.isEmpty() -> {
                viewBinding.etLoginPassword.error = "Password is required"
                false
            }
            else -> true
        }
    }

    private fun performLogin(username: String, password: String) {
        // Query Firestore for the user's email based on the username
        dbFirestore.collection("users")
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    // Get the email associated with the username
                    val email = querySnapshot.documents[0].getString("email") ?: ""

                    // Perform Firebase Authentication using email and password
                    loginUserWithEmail(email, password)
                } else {
                    requireContext().toast("No user found with this username")
                }
            }
            .addOnFailureListener { exception ->
                requireContext().toast("Error: ${exception.message}")
            }


        /*
        if (username == "admin" && password == "admin") {
            Toast.makeText(requireContext(), "Login successful!", Toast.LENGTH_SHORT).show()

            // Navigate to BaseActivity
            val intent = Intent(requireContext(), BaseActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        } else {
            Toast.makeText(requireContext(), "Invalid login credentials", Toast.LENGTH_SHORT).show()
        }
        */
    }

    private fun loginUserWithEmail(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    requireContext().toast("Login successful!")

                    // Navigate to the main activity or home screen
                    val intent = Intent(requireContext(), BaseActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                } else {
                    requireContext().toast("Login failed: ${task.exception?.message}")
                }
            }
            .addOnFailureListener { exception ->
                requireContext().toast("Error: ${exception.message}")
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Avoid memory leaks
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}