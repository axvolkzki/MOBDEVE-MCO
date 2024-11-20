package com.mobdeve.s14.abenoja_delacruz.bookcol

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s14.abenoja_delacruz.bookcol.databinding.ActivityMainBinding
import com.mobdeve.s14.abenoja_delacruz.bookcol.fragments.LoginFragment
import com.mobdeve.s14.abenoja_delacruz.bookcol.fragments.SignupFragment

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val loginFragment = LoginFragment()
        val signupFragment = SignupFragment()


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

        // Temporary solution for navigation between login and signup fragments
        viewBinding.btnLogIn.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                setCustomAnimations(
                    R.anim.slide_in, // Enter animation
                    R.anim.slide_out, // Exit animation
                    R.anim.slide_in, // Pop enter animation
                    R.anim.slide_out  // Pop exit animation
                )



                replace(viewBinding.flWrapper.id, loginFragment)
                addToBackStack(null) // Allows navigating back to MainActivity
                commit()
            }
        }

        viewBinding.btnSignUp.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                setCustomAnimations(
                    R.anim.slide_in,
                    R.anim.slide_out,
                    R.anim.slide_in,
                    R.anim.slide_out
                )


                replace(viewBinding.flWrapper.id, signupFragment)
                addToBackStack(null)
                commit()
            }
        }
    }
}