package com.mobdeve.s14.abenoja_delacruz.bookcol.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.mobdeve.s14.abenoja_delacruz.bookcol.databinding.ActivityScannerBinding
import com.mobdeve.s14.abenoja_delacruz.bookcol.models.AuthorDetails
import com.mobdeve.s14.abenoja_delacruz.bookcol.models.BookResponseModel
import com.mobdeve.s14.abenoja_delacruz.bookcol.utils.RetrofitInstance
import retrofit2.Call
import java.util.concurrent.Executors

class BarcodeScannerActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityScannerBinding
    private val TAG: String = "CHECK_RESPONSE"

    private lateinit var cameraSelector: CameraSelector
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var processCameraProvider: ProcessCameraProvider
    private lateinit var cameraPreview: Preview
    private lateinit var imageAnalysis: ImageAnalysis

    private val cameraExecutor = Executors.newSingleThreadExecutor()

    private var isPreviewActivityStarted = false // flag to check if the preview activity is started

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityScannerBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        cameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(
            {
                try {
                    processCameraProvider = cameraProviderFuture.get()
                    Handler(Looper.getMainLooper()).postDelayed({       // Add a slight delay before binding the camera
                        bindCameraPreview()                             // Rebind preview
                        bindInputAnalyser()                             // Rebind input analyzer
                    }, 100)                                     // 100ms delay for initialization
                } catch (e: Exception) {
                    Log.e(TAG, "Unhandled exception: $e")
                } catch (e: InterruptedException) {
                    Log.e(TAG, "InterruptedException: $e")
                }
            }, ContextCompat.getMainExecutor(this)
        )
    }

    private fun bindCameraPreview() {
        Log.d(TAG, "Binding camera preview...")
        cameraPreview = Preview.Builder()
            .setTargetRotation(viewBinding.previewView.display.rotation)
            .build()
        cameraPreview.setSurfaceProvider(viewBinding.previewView.surfaceProvider)
        try {
            processCameraProvider.bindToLifecycle(this, cameraSelector, cameraPreview)
            Log.d(TAG, "Camera preview bound successfully.")
        } catch (illegalStateException: IllegalStateException) {
            Log.e(TAG, "Error binding camera preview: ${illegalStateException.message}")
        } catch (illegalArgumentException: IllegalArgumentException) {
            Log.e(TAG, "Error binding camera preview: ${illegalArgumentException.message}")
        }
    }


    private fun bindInputAnalyser() {
        Log.d(TAG, "Binding input analyzer...")
        val barcodeScanner: BarcodeScanner = BarcodeScanning.getClient(
            BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_EAN_13)
                .build()
        )
        imageAnalysis = ImageAnalysis.Builder()
            .setTargetRotation(viewBinding.previewView.display.rotation)
            .build()

        imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
            processImageProxy(barcodeScanner, imageProxy)
        }

        try {
            processCameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis)
            Log.d(TAG, "Input analyzer bound successfully.")
        } catch (illegalStateException: IllegalStateException) {
            Log.e(TAG, "Error binding input analyzer: ${illegalStateException.message}")
        } catch (illegalArgumentException: IllegalArgumentException) {
            Log.e(TAG, "Error binding input analyzer: ${illegalArgumentException.message}")
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun processImageProxy(barcodeScanner: BarcodeScanner, imageProxy: ImageProxy) {
        val inputImage = InputImage.fromMediaImage(imageProxy.image!!, imageProxy.imageInfo.rotationDegrees)

        barcodeScanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                if (barcodes.isNotEmpty() && !isPreviewActivityStarted) {
                    isPreviewActivityStarted = true // Prevent duplicate starts

                    // Collect scanned data
                    val barcodeValues = barcodes.map { it.rawValue ?: "" }

                    // Pass data back to AddBookFragment
                    val resultIntent = Intent().apply {
                        putStringArrayListExtra("BARCODES", ArrayList(barcodeValues))
                    }
                    setResult(RESULT_OK, resultIntent)
                    finish()

                    // check if barcode is not empty
                    if (barcodeValues.isNotEmpty()) {
                        // Fetch book data from OpenLibrary API
                        fetchBookDetailsByISBN(barcodeValues[0])
                    } else {
                        Log.e(TAG, "Barcode value is empty.")
                    }

                }
            }
            .addOnFailureListener {
                Log.e(TAG, it.message ?: it.toString())
            }.addOnCompleteListener {
                imageProxy.close()
            }
    }

    private fun fetchBookDetailsByISBN(isbn: String) {
        val call = RetrofitInstance.api.getBookByISBN(isbn) // This hits https://openlibrary.org/isbn/{isbn}.json

        call.enqueue(object : retrofit2.Callback<Map<String, Any>> {
            override fun onResponse(
                call: Call<Map<String, Any>>,
                response: retrofit2.Response<Map<String, Any>>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        // Extract the key (e.g., "/books/OL26344076M")
                        val bookKey = responseBody["key"] as? String
                        if (bookKey != null) {
                            val olid = bookKey.split("/").last() // Extract "OL26344076M"
                            fetchBookDetailsByOLID(olid)
                        } else {
                            Log.e(TAG, "No 'key' field found in response for ISBN: $isbn")
                        }
                    } else {
                        Log.e(TAG, "Response body is null for ISBN: $isbn")
                    }
                } else {
                    Log.e(TAG, "Error resolving ISBN to OLID: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                Log.e(TAG, "Failed to resolve ISBN to OLID: ${t.message}")
            }
        })
    }

    private fun fetchBookDetailsByOLID(olid: String) {
        val call = RetrofitInstance.api.getBookByOLID(olid) // Fetch book by OLID

        call.enqueue(object : retrofit2.Callback<BookResponseModel> {
            override fun onResponse(
                call: Call<BookResponseModel>,
                response: retrofit2.Response<BookResponseModel>
            ) {
                if (response.isSuccessful) {
                    val bookResponse = response.body()
                    if (bookResponse != null) {
                        Log.d(TAG, "[Fetch by OLID] Book fetched successfully: ${bookResponse.title}")


                        // Fetch author details using the first author key
                        // Fetch author names for all authors in the list
                        val authorNames = mutableListOf<String>()

                        // Iterate through the authors list and fetch each author's name
                        val authorKeys = bookResponse.authors?.map { it.key } ?: emptyList()

                        val authorFetchCallbacks = authorKeys.map { authorKey ->
                            if (authorKey != null) {
                                fetchAuthorDetails(authorKey) { authorName ->
                                    // Add the author's name to the list
                                    authorNames.add(authorName)

                                    // If all authors have been fetched, proceed with showing the book details
                                    if (authorNames.size == authorKeys.size) {
                                        Log.d(TAG, "All authors fetched, starting next activity.")

                                        // Once all authors are fetched, pass the data to ScannedBookPreviewActivity
                                        val intent = Intent(this@BarcodeScannerActivity, ScannedBookPreviewActivity::class.java)
                                        intent.putExtra("BOOK_DETAILS", bookResponse)
                                        intent.putStringArrayListExtra("AUTHOR_NAMES", ArrayList(authorNames)) // Pass the list of author names
                                        startActivity(intent)
                                    }
                                }
                            }
                            else {
                                Log.e(TAG, "Author key is null for book with OLID: $olid")
                            }
                        }
                    } else {
                        Log.e(TAG, "Book details are null for OLID: $olid")
                    }
                } else {
                    Log.e(TAG, "Error fetching book details by OLID: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<BookResponseModel>, t: Throwable) {
                Log.e(TAG, "Failed to fetch book details by OLID: ${t.message}")
            }
        })
    }



//    private fun fetchAuthorDetails(authorKey: String, callback: (String) -> Unit) {
//        val url = "https://openlibrary.org$authorKey.json"
//        Log.e(TAG, "Fetching author details from URL: $url")  // Log the full URL
//        val call = RetrofitInstance.api.getAuthorByAuthorKey(authorKey) // Fetch author details using the key
//
//        call.enqueue(object : retrofit2.Callback<AuthorDetails> {
//            override fun onResponse(
//                call: Call<AuthorDetails>,
//                response: retrofit2.Response<AuthorDetails>
//            ) {
//                if (response.isSuccessful) {
//                    val authorDetails = response.body()
//                    if (authorDetails != null) {
//                        Log.d(TAG, "Author fetched successfully: ${authorDetails.name}")
//
//                        // Call the callback with the author's name
//                        callback(authorDetails.name ?: "Unknown Author")
//
//                    } else {
//                        Log.e(TAG, "Author details are null for author key: $authorKey")
//                        callback("Unknown Author")
//                    }
//                } else {
//                    Log.e(TAG, "Error fetching author details by author key: ${response.message()}")
//                    callback("Unknown Author")
//                }
//            }
//
//            override fun onFailure(call: Call<AuthorDetails>, t: Throwable) {
//                Log.e(TAG, "Failed to fetch author details by author key: ${t.message}")
//                callback("Unknown Author")
//            }
//        })
//    }
    private fun fetchAuthorDetails(authorKey: String, callback: (String) -> Unit) {
        // Construct the URL for author details
        val url = "https://openlibrary.org$authorKey.json"
        Log.e(TAG, "Fetching author details from URL: $url")

        // Fetch author details using Retrofit
        val call = RetrofitInstance.api.getAuthorByAuthorKey(authorKey)

        call.enqueue(object : retrofit2.Callback<AuthorDetails> {
            override fun onResponse(
                call: Call<AuthorDetails>,
                response: retrofit2.Response<AuthorDetails>
            ) {
                if (response.isSuccessful) {
                    val authorDetails = response.body()
                    if (authorDetails != null) {
                        // Extract the author's name
                        val authorName = authorDetails.name ?: "Unknown Author"
                        Log.e(TAG, "Author name: $authorName")

                        // Invoke the callback with the name
                        callback(authorName)

                        Log.d(TAG, "Callback invoked with author name: $authorName")

                    } else {
                        Log.e(TAG, "Author details are null for author key: $authorKey")
                        callback("Unknown Author")
                    }
                } else {
                    Log.e(TAG, "Error fetching author details: ${response.message()}")
                    callback("Unknown Author")
                }
            }

            override fun onFailure(call: Call<AuthorDetails>, t: Throwable) {
                Log.e(TAG, "Failed to fetch author details: ${t.message}")
                callback("Unknown Author")
            }
        })
    }



    // Lifecycle methods
    override fun onPause() {
        super.onPause()
        if (::processCameraProvider.isInitialized) {
            processCameraProvider.unbindAll()
        }
    }

    override fun onResume() {
        super.onResume()
//        cameraExecutor.execute { bindCameraPreview() }         isPreviewActivityStarted = false // Reset flag when returning to this activity

        if (::processCameraProvider.isInitialized) {
            bindCameraPreview()                                             // Ensure binding happens again
            bindInputAnalyser()                                             // Ensure the input analyzer is bound
        } else {
            cameraProviderFuture.addListener(
                {
                    try {
                        processCameraProvider = cameraProviderFuture.get()
                        bindCameraPreview()                                 // Rebind preview
                        bindInputAnalyser()                                 // Rebind input analyzer
                    } catch (e: Exception) {
                        Log.e(TAG, "Unhandled exception: $e")
                    }
                }, ContextCompat.getMainExecutor(this)
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::processCameraProvider.isInitialized) {
            processCameraProvider.unbindAll()
        }
        cameraExecutor.shutdown()
    }

    companion object {
        private val TAG = BarcodeScannerActivity::class.simpleName
        private var onScan: ((barcodes: List<Barcode>) -> Unit)? = null

        fun startScanner(context: Context, onScan: (barcodes: List<Barcode>) -> Unit) {
            this.onScan = onScan
            Intent(context, BarcodeScannerActivity::class.java).also {
                context.startActivity(it)
            }
        }
    }
}