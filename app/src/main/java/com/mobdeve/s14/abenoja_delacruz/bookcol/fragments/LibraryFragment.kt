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
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ListenerRegistration
import com.mobdeve.s14.abenoja_delacruz.bookcol.models.Author
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LibraryFragment : Fragment() {
    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!

    private lateinit var firestore: FirebaseFirestore
    private lateinit var libraryAdapter: LibraryAdapter
    private val booksList = ArrayList<BookResponseModel>()

    private var libraryListener: ListenerRegistration? = null

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

        // Attach real-time listener
        attachLibraryListener()
    }

    private fun setupRecyclerView() {
        binding.rcvLibrary.layoutManager = GridLayoutManager(requireContext(), 2)
        libraryAdapter = LibraryAdapter(booksList)
        binding.rcvLibrary.adapter = libraryAdapter
    }

    private fun attachLibraryListener() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            showLoading()

            // Detach any existing listener to prevent duplicates
            libraryListener?.remove()

            libraryListener = firestore.collection(FirestoreReferences.LIBRARY_COLLECTION)
                .whereEqualTo(FirestoreReferences.LIBRARY_USERID_FIELD, userId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.e(TAG, "Error fetching library data: ${error.message}")
                        hideLoading()
                        return@addSnapshotListener
                    }

                    if (snapshot != null) {
                        val bookIds = snapshot.documents.mapNotNull { it.getString(FirestoreReferences.LIBRARY_BOOKID_FIELD) }

                        // Fetch book details
                        fetchBooksDetails(bookIds)
                    } else {
                        hideLoading()
                    }
                }
        } else {
            Log.e(TAG, "User ID is null, cannot fetch library.")
        }
    }

    private fun fetchBooksDetails(bookIds: List<String>) {
        if (bookIds.isEmpty()) {
            booksList.clear() // Clear the list when there are no books
            libraryAdapter.notifyDataSetChanged()
            hideLoading()
            return
        }

        // Fetch details for all books in a batch
        firestore.collection(FirestoreReferences.BOOK_COLLECTION)
            .whereIn(FieldPath.documentId(), bookIds)
            .get()
            .addOnSuccessListener { querySnapshot ->
                booksList.clear() // Clear existing books before adding new ones

                for (document in querySnapshot.documents) {
                    val data = document.data
                    if (data != null) {
                        val book = BookResponseModel(
                            key = data["key"] as? String,
                            title = data["title"] as? String,
                            authors = (data["authors"] as? List<*>)?.mapNotNull { Author(it.toString()) },
                            covers = (data["covers"] as? List<*>)?.mapNotNull { it.toString().toLongOrNull() },
                            publish_date = data["publish_date"] as? String,
                            number_of_pages = (data["number_of_pages"] as? Number)?.toInt(),
                            publishers = (data["publishers"] as? List<*>)?.mapNotNull { it.toString() },
                            isbn_13 = (data["isbn_13"] as? List<*>)?.mapNotNull { it.toString() },
                            description = data["description"] as? String,
                            subjects = (data["subjects"] as? List<*>)?.mapNotNull { it.toString() }
                        )
                        booksList.add(book) // Add all books directly to booksList
                    }
                }

                libraryAdapter.notifyDataSetChanged()
                Log.d(TAG, "Books successfully fetched: ${booksList.size}")
                hideLoading()
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error fetching book details: ", exception)
                hideLoading()
            }
    }



    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        // Detach listener to prevent memory leaks
        libraryListener?.remove()
    }

    companion object {
        private const val TAG = "LibraryFragment"
    }
}


