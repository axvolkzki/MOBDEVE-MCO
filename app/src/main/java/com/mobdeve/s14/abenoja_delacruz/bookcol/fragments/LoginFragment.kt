package com.mobdeve.s14.abenoja_delacruz.bookcol.fragments

import android.content.Context
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
import com.mobdeve.s14.abenoja_delacruz.bookcol.models.UserModel
import com.mobdeve.s14.abenoja_delacruz.bookcol.utils.FirestoreReferences
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

    // DB Schema
    private lateinit var mAuth: FirebaseAuth        // Firebase Authentication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

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
        if (!validateInput(username, password)) return

        // Perform login
        performLogin(username, password)
    }

    private fun validateInput(username: String, password: String): Boolean {
        if (username == null || password == null) {
            requireContext().toast("Please fill in all fields")
            return false
        }


        return when {
            username.isBlank() -> {
                viewBinding.etLoginUsername.error = "Username is required"
                false
            }
            password.isBlank() -> {
                viewBinding.etLoginPassword.error = "Password is required"
                false
            }
            password.length < 6 -> {
                viewBinding.etLoginPassword.error = "Password must be at least 6 characters"
                false
            }
            else -> true
        }
    }

    private fun performLogin(username: String, password: String) {
        // Use FirebaseFirestore to check if the user exists
        FirebaseFirestore.getInstance()
            .collection(FirestoreReferences.USER_COLLECTION)
            .whereEqualTo(FirestoreReferences.USERNAME_FIELD, username)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val email = querySnapshot.documents[0].getString(FirestoreReferences.EMAIL_FIELD) ?: ""
                    loginUserWithEmail(email, password)
                } else {
                    requireContext().toast("No user found with this username")
                }
            }
            .addOnFailureListener { exception ->
                requireContext().toast("Error: ${exception.message}")
            }
    }

    private fun loginUserWithEmail(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val userId = mAuth.currentUser?.uid ?: ""

                    // Fetch UserModel for additional data if needed
                    fetchUserData(userId)

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

    private fun fetchUserData(userId: String) {
        FirebaseFirestore.getInstance()
            .collection(FirestoreReferences.USER_COLLECTION)
            .document(userId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val user = documentSnapshot.toObject(UserModel::class.java)
                if (user != null) {
                    saveUserDataToPreferences(user)
                }
            }
            .addOnFailureListener { exception ->
                requireContext().toast("Error fetching user data: ${exception.message}")
            }
    }

    private fun saveUserDataToPreferences(user: UserModel) {
        val sharedPreferences = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("userId", user.userId)
            putString("username", user.username)
            putString("email", user.email)
            apply()
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