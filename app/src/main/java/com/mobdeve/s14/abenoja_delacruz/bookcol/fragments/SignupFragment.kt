package com.mobdeve.s14.abenoja_delacruz.bookcol.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.mobdeve.s14.abenoja_delacruz.bookcol.models.UserModel
import com.mobdeve.s14.abenoja_delacruz.bookcol.utils.FirestoreReferences
import com.mobdeve.s14.abenoja_delacruz.bookcol.utils.toast

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

    private var _binding: FragmentSignupBinding? = null
    private val viewBinding get() = _binding!!

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        // Inflate the layout for this fragment
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle sign-up button click
        viewBinding.btnSignUpSubmit.setOnClickListener {
            handleSignUp()
        }

        // Navigate back to the LoginFragment
        viewBinding.btnSignUpToLogin.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.slide_out, R.anim.slide_in, R.anim.slide_out)
                .replace(R.id.flWrapper, LoginFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun handleSignUp() {
        val username = viewBinding.etSignupInputUsername.text.toString().trim()
        val email = viewBinding.etSignupInputEmail.text.toString().trim()
        val password = viewBinding.etSignupInputPassword.text.toString().trim()
        val confirmPassword = viewBinding.etSignupInputConfirmPassword.text.toString().trim()

        // Validate input, flag error handling but do not close the app
        if (!validateInput(username, email, password, confirmPassword)) return

        // Check if username already exists
        FirebaseFirestore.getInstance()
            .collection(FirestoreReferences.USER_COLLECTION)
            .whereEqualTo(FirestoreReferences.USERNAME_FIELD, username)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    // Proceed with user registration
                    registerNewUser(username, email, password)
                } else {
                    viewBinding.etSignupInputUsername.error = "Username is already taken"
                }
            }
            .addOnFailureListener { exception ->
                requireContext().toast("Error checking username: ${exception.message}")
            }
    }

    private fun validateInput(username: String?, email: String?, password: String?, confirmPassword: String?): Boolean {
        if (username == null || email == null || password == null || confirmPassword == null) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return false
        }

        return when {
            username.isBlank() -> {
                viewBinding.etSignupInputUsername.error = "Username is required"
                false
            }
            email.isBlank() -> {
                viewBinding.etSignupInputEmail.error = "Email is required"
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                viewBinding.etSignupInputEmail.error = "Invalid email address"
                false
            }
            password.isBlank() -> {
                viewBinding.etSignupInputPassword.error = "Password is required"
                false
            }
            confirmPassword.isBlank() -> {
                viewBinding.etSignupInputConfirmPassword.error = "Please confirm your password"
                false
            }
            password != confirmPassword -> {
                viewBinding.etSignupInputConfirmPassword.error = "Passwords do not match"
                false
            }
            password.length < 6 -> {
                viewBinding.etSignupInputPassword.error = "Password must be at least 6 characters"
                false
            }
            else -> true
        }
    }


    private fun registerNewUser(username: String, email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val userId = mAuth.currentUser?.uid
                    if (userId != null) {
                        val userModel = UserModel(
                            userId = userId,
                            username = username,
                            email = email
                        )
                        saveUserToFirestore(userModel)
                    } else {
                        requireContext().toast("Error: Unable to get user ID")
                    }
                } else {
                    requireContext().toast("Sign up failed: ${task.exception?.message}")
                }
            }
            .addOnFailureListener { exception ->
                requireContext().toast("Error: ${exception.message}")
            }
    }

    private fun saveUserToFirestore(userModel: UserModel) {
        FirebaseFirestore.getInstance()
            .collection(FirestoreReferences.USER_COLLECTION)
            .document(userModel.userId)
            .set(userModel)
            .addOnSuccessListener {
                saveUserIdToPreferences(userModel.userId)
                requireContext().toast("Sign up successful!")

                // Navigate to main activity
                val intent = Intent(requireContext(), BaseActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }
            .addOnFailureListener { exception ->
                requireContext().toast("Error saving user data: ${exception.message}")
            }
    }

    private fun saveUserIdToPreferences(userId: String) {
        val sharedPreferences = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("userId", userId)
            apply()
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