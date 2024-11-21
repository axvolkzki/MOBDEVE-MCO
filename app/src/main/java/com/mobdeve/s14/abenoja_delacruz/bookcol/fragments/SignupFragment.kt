package com.mobdeve.s14.abenoja_delacruz.bookcol.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mobdeve.s14.abenoja_delacruz.bookcol.R
import com.mobdeve.s14.abenoja_delacruz.bookcol.activities.BaseActivity
import com.mobdeve.s14.abenoja_delacruz.bookcol.databinding.FragmentSignupBinding
import com.mobdeve.s14.abenoja_delacruz.bookcol.helpers.toast
import com.mobdeve.s14.abenoja_delacruz.bookcol.models.UserModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignupFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var viewBinding : FragmentSignupBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var dbFirestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        // Inflate the layout for this fragment
        viewBinding = FragmentSignupBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.viewBinding = FragmentSignupBinding.bind(view)

        this.viewBinding.btnSignUpSubmit.setOnClickListener {
            handleSignUp()
        }

        // Navigate back to the LoginFragment
        this.viewBinding.btnSignUpToLogin.setOnClickListener {
            parentFragmentManager.beginTransaction()
                // slide in from the bottom
                .setCustomAnimations(R.anim.slide_in, R.anim.slide_out, R.anim.slide_in, R.anim.slide_out)
                .replace(R.id.flWrapper, LoginFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun handleSignUp() {
        val username = viewBinding.etSignupInputUsername.text.toString()
        val email = viewBinding.etSignupInputEmail.text.toString()
        val password = viewBinding.etSignupInputPassword.text.toString()
        val confirmPassword = viewBinding.etSignupInputConfirmPassword.text.toString()

        // Validate inputs
        if (username.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(requireContext(), "Invalid email address", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(requireContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if username already exists in Firestore
        dbFirestore.collection("users")
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    // Username is unique, now create the user in Firebase Authentication
                    registerNewUser(username, email, password)
                } else {
                    // Username already exists
                    Toast.makeText(requireContext(), "Username is already taken", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Error checking username: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun registerNewUser(username: String, email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser

                    Log.d("Signup", "User signed in successfully: ${user?.uid}")

                    // Store additional user data (username) in Firestore
                    val userData = hashMapOf(
                        "username" to username,
                        "email" to email
                    )

                    dbFirestore.collection("users")
                        .document(user!!.uid) // Use UID from FirebaseAuth as document ID
                        .set(userData)
                        .addOnSuccessListener {
                            // Successfully registered and stored user data
                            Toast.makeText(requireContext(), "Sign up successful!", Toast.LENGTH_SHORT).show()

                            val intent = Intent(requireContext(), BaseActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                            startActivity(intent)
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(requireContext(), "Error saving user data: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(requireContext(), "Sign up failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    Log.d("Signup", "Sign up failed: ${task.exception?.message}")
                }
            }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SignupFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignupFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}