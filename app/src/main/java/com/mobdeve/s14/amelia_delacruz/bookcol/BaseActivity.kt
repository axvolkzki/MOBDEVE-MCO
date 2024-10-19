package com.mobdeve.s14.amelia_delacruz.bookcol

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mobdeve.s14.amelia_delacruz.bookcol.databinding.ActivityBaseBinding
import com.mobdeve.s14.amelia_delacruz.bookcol.fragments.AddBookFragment
import com.mobdeve.s14.amelia_delacruz.bookcol.fragments.BooksFragment
import com.mobdeve.s14.amelia_delacruz.bookcol.fragments.FeedFragment
import com.mobdeve.s14.amelia_delacruz.bookcol.fragments.ProfileFragment
import com.mobdeve.s14.amelia_delacruz.bookcol.fragments.WishlistFragment

class BaseActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val booksFragment = BooksFragment()
        val feedFragment = FeedFragment()
        val addBookFragment = AddBookFragment()
        val wishlistFragment = WishlistFragment()
        val profileFragment = ProfileFragment()
        val fragmentManager = supportFragmentManager

        
        makeCurrentFragment(booksFragment)

        viewBinding.bnvNavbar.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_shelf -> makeCurrentFragment(booksFragment)
                R.id.nav_feed -> makeCurrentFragment(feedFragment)
                R.id.nav_add_book -> makeCurrentFragment(addBookFragment)
                R.id.nav_wishlist -> makeCurrentFragment(wishlistFragment)
                R.id.nav_profile -> makeCurrentFragment(profileFragment)
                else -> return@setOnNavigationItemSelectedListener false
            }
            true
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(viewBinding.flWrapper.id, fragment)
            commit()
        }
    }
}