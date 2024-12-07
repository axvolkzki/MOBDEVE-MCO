package com.mobdeve.s14.abenoja_delacruz.bookcol.fragments

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class LibraryFragment : Fragment() {
    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!

    private lateinit var firestore: FirebaseFirestore
    private lateinit var libraryAdapter: LibraryAdapter
    private val booksList = ArrayList<BookResponseModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()

        // Set up RecyclerView
        binding.rcvLibrary.layoutManager = GridLayoutManager(requireContext(), 2)

        // Initialize the adapter and set it to the RecyclerView
        libraryAdapter = LibraryAdapter(booksList)
        binding.rcvLibrary.adapter = libraryAdapter

        // Fetch the current user's ID from Firebase Authentication
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            Log.d(TAG, "User ID: $userId")
            // Fetch books data from Firestore
            fetchBooksFromLibrary(userId)
        } else {
            Log.e(TAG, "User is not authenticated")
        }
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
                    // Log the raw document data first
                    Log.d(TAG, "Raw document data: ${document.data}")

                    // Manually handle the publishers field
                    val publishersField = document.get(FirestoreReferences.PUBLISHERS_FIELD)
                    val publishers: List<String>? = when (publishersField) {
                        is String -> listOf(publishersField)
                        is List<*> -> publishersField.filterIsInstance<String>()
                        else -> null
                    }

                    // Manually handle the subjects field as it is an array
                    val subjectsField = document.get(FirestoreReferences.SUBJECTS_FIELD)
                    val subjects: List<String> = when (subjectsField) {
                        is String -> listOf(subjectsField) // If it's a single string, wrap it in a list
                        is List<*> -> subjectsField.filterIsInstance<String>() // Filter only strings from the list
                        else -> listOf() // Default to an empty list
                    }

                    Log.d(TAG, "Raw subjects field: $subjectsField (${subjectsField?.javaClass?.name})")

                    // Handle covers field
                    val coversField = document.get(FirestoreReferences.COVERS_FIELD)
                    val covers: List<Long>? = when (coversField) {
                        is Long -> listOf(coversField)
                        is List<*> -> coversField.filterIsInstance<Long>()
                        else -> null
                    }

                    // Handle ISBN-13 field - ensure it's always a List<String>
                    val isbn13Field = document.get(FirestoreReferences.ISBN_13_FIELD)
                    val isbn13List: List<String>? = when (isbn13Field) {
                        is String -> listOf(isbn13Field)
                        is List<*> -> isbn13Field.filterIsInstance<String>()
                        else -> null
                    }

                    Log.e(TAG, "Raw ISBN-13 field: $isbn13Field (${isbn13Field?.javaClass?.name})")

                    // Handle Authors
                    val authorsField = document.get(FirestoreReferences.AUTHORS_FIELD)
                    val authors: List<Author>? = when (authorsField) {
                        is String -> listOf(Author(authorsField))  // If it's a single string, wrap it in a List<Author>
                        is List<*> -> authorsField.filterIsInstance<String>().map { Author(it) }  // If it's a List<String>, convert each string to an Author
                        else -> null  // If it's neither, return null or empty list
                    }

//                    val book = document.toObject(BookResponseModel::class.java)
                    val book = document.toObject(BookResponseModel::class.java)
                        ?.copy(
                            publishers = publishers,
                            subjects = subjects,
                            covers = covers,
                            isbn_13 = isbn13List,
                            authors = authors,
                        )
                    book?.let {
                        booksList.add(it)
                        libraryAdapter.notifyDataSetChanged()
                    }

                    Log.d(TAG, "Parsed book details: $book")
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error getting book details: ", exception)
                }
        }
    }
}

