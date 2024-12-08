package com.mobdeve.s14.abenoja_delacruz.bookcol.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mobdeve.s14.abenoja_delacruz.bookcol.R
import com.mobdeve.s14.abenoja_delacruz.bookcol.databinding.ActivityBaseBinding
import com.mobdeve.s14.abenoja_delacruz.bookcol.fragments.AddBookFragment
import com.mobdeve.s14.abenoja_delacruz.bookcol.fragments.LibraryFragment
import com.mobdeve.s14.abenoja_delacruz.bookcol.fragments.FeedFragment
import com.mobdeve.s14.abenoja_delacruz.bookcol.fragments.ProfileFragment
import com.mobdeve.s14.abenoja_delacruz.bookcol.fragments.WishlistFragment

class BaseActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityBaseBinding

    private val libraryFragment = LibraryFragment()
    private val feedFragment = FeedFragment()
    private val addBookFragment = AddBookFragment()
    private val wishlistFragment = WishlistFragment()
    private val profileFragment = ProfileFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Only create initial fragment if this is the first creation
        if (savedInstanceState == null) {
            makeCurrentFragment(libraryFragment)
            viewBinding.txvPageTitle.text = "Books"
        }

        viewBinding.bnvNavbar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_shelf -> {
                    makeCurrentFragment(libraryFragment)
                    viewBinding.txvPageTitle.text = "Books"
                }
                R.id.nav_feed -> {
                    makeCurrentFragment(feedFragment)
                    viewBinding.txvPageTitle.text = "Feed"
                }
                R.id.nav_add_book -> {
                    makeCurrentFragment(addBookFragment)
                    viewBinding.txvPageTitle.text = "Add Book"
                }
                R.id.nav_wishlist -> {
                    makeCurrentFragment(wishlistFragment)
                    viewBinding.txvPageTitle.text = "Wishlist"
                }
                R.id.nav_profile -> {
                    makeCurrentFragment(profileFragment)
                    viewBinding.txvPageTitle.text = "Profile"
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
            true
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) {
        // Don't replace if it's the same fragment
        var currentFragment = supportFragmentManager.findFragmentById(viewBinding.flWrapper.id)
        if (currentFragment?.javaClass == fragment.javaClass) return

        currentFragment = fragment
        supportFragmentManager.beginTransaction().apply {
            replace(viewBinding.flWrapper.id, fragment)
            commit()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save current fragment state if needed
    }
}
