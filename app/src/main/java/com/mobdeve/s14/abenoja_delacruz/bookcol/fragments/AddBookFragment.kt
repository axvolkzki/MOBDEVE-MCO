package com.mobdeve.s14.abenoja_delacruz.bookcol.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s14.abenoja_delacruz.bookcol.activities.BarcodeScannerActivity
import com.mobdeve.s14.abenoja_delacruz.bookcol.databinding.FragmentAddBookBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddBookFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddBookFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val cameraPermission = android.Manifest.permission.CAMERA       // Camera permission
    private lateinit var viewBinding : FragmentAddBookBinding               // ViewBinding

    // Request permission launcher for camera
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted
            // Toast message for debugging
            Toast.makeText(requireContext(), "Camera permission granted", Toast.LENGTH_SHORT).show()

            // Start Scanner
            startScanner()
        } else {
            // Permission is not granted
            // Toast message for debugging
            Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private val scannerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val barcodes = result.data?.getStringArrayListExtra("BARCODES")
                barcodes?.let { handleScannedBarcodes(it) }
            } else {
                Toast.makeText(requireContext(), "No barcode scanned", Toast.LENGTH_SHORT).show()
            }
        }


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
        // Inflate the layout for this fragment
        viewBinding = FragmentAddBookBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.viewBinding = FragmentAddBookBinding.bind(view)

        this.viewBinding.llScanBarCode.setOnClickListener {
            if (checkCameraHardware(requireContext())) {
                requestCameraAndStartScanner()
            } else {
                // Toast message for debugging
                Toast.makeText(requireContext(), "No camera available", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkCameraHardware(context: Context) : Boolean {
        if (context.packageManager.hasSystemFeature(android.content.pm.PackageManager.FEATURE_CAMERA_ANY)) {
            return true
        } else {
            return false
        }
    }


    private fun requestCameraAndStartScanner() {
        if (isPermissionGranted(cameraPermission)) {
            startScanner()
        } else {
            requestCameraPermission()
        }
    }

    private fun isPermissionGranted(cameraPermission: String): Boolean {
        return requireContext().checkSelfPermission(cameraPermission) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        when {
            shouldShowRequestPermissionRationale(cameraPermission) -> {
                cameraPermissionRequest(
                    positive = { openPermissionSetting() }
                )
                // Toast message for debugging
                Toast.makeText(requireContext(), "Camera permission is needed to scan barcodes", Toast.LENGTH_SHORT).show()
            }
            else -> {
                // Request permission
                requestPermissionLauncher.launch(cameraPermission)
            }
        }
    }

    private fun openPermissionSetting() {
        val intent = android.content.Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = android.net.Uri.fromParts("package", requireContext().packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    private fun cameraPermissionRequest(positive: () -> Unit) {
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Camera Permission")
            .setMessage("Camera permission is needed to scan barcodes")
            .setPositiveButton("OK") { _, _ ->
                positive()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }


    private fun startScanner() {
        val intent = Intent(requireContext(), BarcodeScannerActivity::class.java)
        scannerLauncher.launch(intent)
    }

    private fun handleScannedBarcodes(barcodes: List<String>) {
        barcodes.forEach { barcode ->
            // Update the UI or perform any action with the barcode
            // Toast message for debugging
            Toast.makeText(requireContext(), "Scanned: $barcode", Toast.LENGTH_SHORT).show()
        }
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddBookFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddBookFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}