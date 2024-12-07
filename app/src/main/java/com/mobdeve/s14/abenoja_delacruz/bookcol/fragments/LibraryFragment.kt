package com.mobdeve.s14.abenoja_delacruz.bookcol.fragments

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.mobdeve.s14.abenoja_delacruz.bookcol.adapters.LibraryAdapter
import com.mobdeve.s14.abenoja_delacruz.bookcol.databinding.FragmentLibraryBinding
import com.mobdeve.s14.abenoja_delacruz.bookcol.models.BookResponseModel
import com.mobdeve.s14.abenoja_delacruz.bookcol.utils.FirestoreReferences


/**
 * A simple [Fragment] subclass.
 * Use the [LibraryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
import com.google.firebase.auth.FirebaseAuth
import com.mobdeve.s14.abenoja_delacruz.bookcol.models.Author
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LibraryFragment : Fragment() {
    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!

    private lateinit var firestore: FirebaseFirestore
    private lateinit var libraryAdapter: LibraryAdapter
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
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup RecyclerView
        setupRecyclerView()

        // Only load data if it hasn't been loaded yet
        if (!isDataLoaded) {
            loadLibraryData()
        }
    }

    private fun setupRecyclerView() {
        binding.rcvLibrary.layoutManager = GridLayoutManager(requireContext(), 2)
        libraryAdapter = LibraryAdapter(booksList)
        binding.rcvLibrary.adapter = libraryAdapter
    }

    private fun loadLibraryData() {
        showLoading()

        lifecycleScope.launch(Dispatchers.Main) {
            try {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId != null) {
                    // Clear existing data before loading
                    booksList.clear()
                    libraryAdapter.notifyDataSetChanged()

                    fetchBooksFromLibrary(userId)
                    isDataLoaded = true
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading library data: ", e)
                Toast.makeText(context, "Error loading books", Toast.LENGTH_SHORT).show()
            } finally {
                hideLoading()
            }
        }
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    fun refreshData() {
        isDataLoaded = false
        loadLibraryData()
    }

    companion object {
        private const val TAG = "LibraryFragment"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Fetch books data from Firestore
    private fun fetchBooksFromLibrary(userId: String) {
        firestore.collection(FirestoreReferences.LIBRARY_COLLECTION)
            .whereEqualTo(FirestoreReferences.LIBRARY_USERID_FIELD, userId)  // Filter by userId
            .get()
            .addOnSuccessListener { documents ->
                val bookIds = mutableListOf<String>()
                for (document in documents) {
                    val bookId = document.getString(FirestoreReferences.LIBRARY_BOOKID_FIELD)
                    bookId?.let {
                        bookIds.add(it)
                    }
                }
                // After collecting all the book IDs, fetch the books
                fetchBooksDetails(bookIds)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting books from Library collection: ", exception)
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
                        libraryAdapter.notifyDataSetChanged()

                        Log.d(TAG, "Successfully added book: ${book.title}")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error getting book details: ", exception)
                }
        }
    }
}

