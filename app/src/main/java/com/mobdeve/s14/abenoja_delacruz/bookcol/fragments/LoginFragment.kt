package com.mobdeve.s14.abenoja_delacruz.bookcol.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.mobdeve.s14.abenoja_delacruz.bookcol.R
import com.mobdeve.s14.abenoja_delacruz.bookcol.activities.BaseActivity
import com.mobdeve.s14.abenoja_delacruz.bookcol.databinding.FragmentLoginBinding

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            val username = viewBinding.etLoginUsername.text.toString()
            val password = viewBinding.etLoginPassword.text.toString()

            if (validateInput(username, password)) {
                performLogin(username, password)
            }
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
        // Simulated login logic (Replace with Firebase Authentication)
        if (username == "admin" && password == "admin") {
            Toast.makeText(requireContext(), "Login successful!", Toast.LENGTH_SHORT).show()

            // Navigate to BaseActivity
            val intent = Intent(requireContext(), BaseActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        } else {
            Toast.makeText(requireContext(), "Invalid login credentials", Toast.LENGTH_SHORT).show()
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