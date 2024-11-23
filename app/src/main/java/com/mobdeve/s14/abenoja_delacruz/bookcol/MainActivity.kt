package com.mobdeve.s14.abenoja_delacruz.bookcol

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mobdeve.s14.abenoja_delacruz.bookcol.databinding.ActivityMainBinding
import com.mobdeve.s14.abenoja_delacruz.bookcol.fragments.LoginFragment
import com.mobdeve.s14.abenoja_delacruz.bookcol.fragments.SignupFragment

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Test crash button for Firebase Crashlytics
        /*
        val crashButton = Button(this)
        crashButton.text = "Test Crash"
        crashButton.setOnClickListener {
            throw RuntimeException("Test Crash") // Force a crash
        }

        addContentView(crashButton, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT))
         */


        // If user is not logged in, show the main activity layout
//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction().apply {
//                replace(viewBinding.flWrapper.id, loginFragment)
//                commit()
//            }
//        }

        // If user is logged in, show the base activity layout
//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction().apply {
//                replace(viewBinding.flWrapper.id, BaseActivity())
//                commit()
//            }
//        }

        // When user is not logged in, show the login and signup buttons
        viewBinding.btnLogIn.setOnClickListener {
            navigateToFragment(LoginFragment())
        }

        // Navigate to SignupFragment on button click
        viewBinding.btnSignUp.setOnClickListener {
            navigateToFragment(SignupFragment())
        }
    }

    // Helper function to handle fragment navigation
    private fun navigateToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in, // Enter animation
                R.anim.slide_out, // Exit animation
                R.anim.slide_in, // Pop enter animation
                R.anim.slide_out  // Pop exit animation
            )
            .replace(viewBinding.flWrapper.id, fragment) // Replace the current fragment
            .addToBackStack(null) // Add transaction to the back stack
            .commit()
    }

    override fun onBackPressed() {
        // Handle back navigation
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack() // Navigate to the previous fragment
        } else {
            super.onBackPressed() // Exit the app
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onPause() {
        super.onPause()

    }
}