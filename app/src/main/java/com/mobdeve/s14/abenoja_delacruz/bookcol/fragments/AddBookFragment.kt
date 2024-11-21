package com.mobdeve.s14.abenoja_delacruz.bookcol.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.mobdeve.s14.abenoja_delacruz.bookcol.R
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

    private lateinit var viewBinding : FragmentAddBookBinding

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
        return inflater.inflate(R.layout.fragment_add_book, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.viewBinding = FragmentAddBookBinding.bind(view)

        this.viewBinding.llScanBarCode.setOnClickListener {
            if (checkCameraHardware(requireContext())) {
                checkCameraPermission()
                val camera = getCameraInstance()
                if (camera != null) {
                    camera.release()
                }
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

    private fun checkCameraPermission() {
        if (androidx.core.content.ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            // Permission is granted
            // Toast message for debugging
            Toast.makeText(requireContext(), "Camera permission granted", Toast.LENGTH_SHORT).show()
        } else {
            // Permission is not granted
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), 1)
        }
    }

    private fun getCameraInstance() : android.hardware.Camera? {
        var c : android.hardware.Camera? = null
        try {
            c = android.hardware.Camera.open()
        } catch (e : Exception) {
            // Camera is not available (in use or does not exist)
            // Toast message for debugging
            Toast.makeText(requireContext(), "Camera is not available", Toast.LENGTH_SHORT).show()
        }
        return c
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