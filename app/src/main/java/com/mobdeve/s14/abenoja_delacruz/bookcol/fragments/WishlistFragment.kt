package com.mobdeve.s14.abenoja_delacruz.bookcol.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mobdeve.s14.abenoja_delacruz.bookcol.adapters.WishlistAdapter
import com.mobdeve.s14.abenoja_delacruz.bookcol.databinding.FragmentWishlistBinding
import com.mobdeve.s14.abenoja_delacruz.bookcol.models.Author
import com.mobdeve.s14.abenoja_delacruz.bookcol.models.BookResponseModel
import com.mobdeve.s14.abenoja_delacruz.bookcol.utils.FirestoreReferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * Use the [WishlistFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WishlistFragment : Fragment() {
    private var _binding: FragmentWishlistBinding? = null
    private val binding get() = _binding!!

    private lateinit var firestore: FirebaseFirestore
    private lateinit var wishlistAdapter: WishlistAdapter
    private val booksList = ArrayList<BookResponseModel>()

    // Add flag to track if data has been loaded
    private var isDataLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWishlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the RecyclerView
        setUpRecyclerView()

        // Load data from Firestore
        if (!isDataLoaded) {
            loadWishlistData()
        }

    }

    private fun setUpRecyclerView() {
        // Set up the RecyclerView
        binding.rcvWishlists.layoutManager = GridLayoutManager(context, 2)
        wishlistAdapter = WishlistAdapter(booksList)
        binding.rcvWishlists.adapter = wishlistAdapter
    }

    private fun loadWishlistData() {
        showLoading()

        lifecycleScope.launch(Dispatchers.Main) {
            try {
                val userId = FirebaseAuth.getInstance().currentUser?.uid

                if (userId != null) {
                    // Clear existing data before loading new data
                    booksList.clear()
                    wishlistAdapter.notifyDataSetChanged()

                    fetchBooksFromWishlist(userId)
                    isDataLoaded = true
                }
            } catch (e: Exception) {
                Log.e("WishlistFragment", "Error loading data: ${e.message}")
                Toast.makeText(context, "Error loading data", Toast.LENGTH_SHORT).show()
            } finally {
                hideLoading()
            }
        }
    }



    private fun hideLoading() {
        binding.progressBarWishlist.visibility = View.GONE
    }

    private fun showLoading() {
        binding.progressBarWishlist.visibility = View.VISIBLE
    }

    private fun fetchBooksFromWishlist(userId: String) {
        firestore.collection(FirestoreReferences.WISHLIST_COLLECTION)
            .whereEqualTo(FirestoreReferences.WISHLIST_USERID_FIELD, userId)  // Filter by userId
            .get()
            .addOnSuccessListener { documents ->
                val bookIds = mutableListOf<String>()
                for (document in documents) {
                    val bookId = document.getString(FirestoreReferences.WISHLIST_BOOKID_FIELD)
                    bookId?.let {
                        bookIds.add(it)
                    }
                }
                // After collecting all the book IDs, fetch the books
                fetchBooksDetails(bookIds)
            }
            .addOnFailureListener { exception ->
                Log.e(WishlistFragment.TAG, "Error getting books from Library collection: ", exception)
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchBooksDetails(bookIds: List<String>) {
        for (bookId in bookIds) {
            firestore.collection(FirestoreReferences.BOOK_COLLECTION)
                .document(bookId)
                .get()
                .addOnSuccessListener { document ->
                    val data = document.data
                    if (data != null) {
                        // Handle each field individually
                        val key = data["key"] as? String
                        val title = data["title"] as? String

                        // Handle authors
                        val authorsRaw = data["authors"] as? List<*>
                        val authors = authorsRaw?.map { Author(it.toString()) } ?: listOf()

                        // Handle covers
                        val coversRaw = data["covers"] as? List<*>
                        val covers = coversRaw?.mapNotNull { it.toString().toLongOrNull() }

                        // Handle other fields
                        val publishDate = data["publish_date"] as? String
                        val numberOfPages = (data["number_of_pages"] as? Number)?.toInt()

                        val publishersRaw = data["publishers"] as? List<*>
                        val publishers = publishersRaw?.mapNotNull { it.toString() }

                        val isbn13Raw = data["isbn_13"] as? List<*>
                        val isbn13 = isbn13Raw?.mapNotNull { it.toString() }

                        val description = data["description"] as? String

                        val subjectsRaw = data["subjects"] as? List<*>
                        val subjects = subjectsRaw?.mapNotNull { it.toString() }

                        // Create BookResponseModel manually
                        val book = BookResponseModel(
                            key = key,
                            title = title,
                            authors = authors,
                            covers = covers,
                            publish_date = publishDate,
                            number_of_pages = numberOfPages,
                            publishers = publishers,
                            isbn_13 = isbn13,
                            description = description,
                            subjects = subjects
                        )

                        booksList.add(book)
                        wishlistAdapter.notifyDataSetChanged()

                        Log.d(WishlistFragment.TAG, "Successfully added book: ${book.title}")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e(WishlistFragment.TAG, "Error getting book details: ", exception)
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "WishlistFragment"
    }
}