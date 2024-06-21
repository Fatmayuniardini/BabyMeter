package com.capstone.babymeter.ui.predictions

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import com.capstone.babymeter.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

class InputDataFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var imageView: ImageView
    private val PICK_IMAGE = 1
    private val TAKE_PHOTO = 2
    private val MY_PERMISSIONS_REQUEST_CAMERA = 100
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_input_data, container, false)

        imageView = view.findViewById(R.id.image_add)
        progressBar = view.findViewById(R.id.progressBar)

        val buttonGallery: Button = view.findViewById(R.id.gallery_button)
        buttonGallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE)
        }

        val buttonCamera: Button = view.findViewById(R.id.btn_camera)
        buttonCamera.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.CAMERA
                    )
                ) {
                } else {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.CAMERA),
                        MY_PERMISSIONS_REQUEST_CAMERA
                    )
                }
            } else {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, TAKE_PHOTO)
            }
        }

        val scanBabyButton: Button = view.findViewById(R.id.upload_button)
        scanBabyButton.setOnClickListener {
            // Get the input data
            val nik = view.findViewById<EditText>(R.id.nikEditText).text.toString()
            val name = view.findViewById<EditText>(R.id.nameEditText).text.toString()
            val age = view.findViewById<EditText>(R.id.ageEditText).text.toString().toInt()
            val weight = view.findViewById<EditText>(R.id.weightEditText).text.toString().toFloat()

            // Get the image from imageView
            val bitmap = imageView.drawable.toBitmap()

            progressBar.visibility = View.VISIBLE

            // Send the input data to the backend
            sendInputDataToBackend(nik, name, age, weight, bitmap)

        }

        return view
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_CAMERA -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, TAKE_PHOTO)
                } else {
                    Toast.makeText(context, "Permission denied to use Camera", Toast.LENGTH_SHORT)
                        .show()
                }
                return
            }

            else -> {
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && data != null) {
            var bitmap: Bitmap? = null
            if (requestCode == PICK_IMAGE) {
                val inputStream: InputStream? =
                    context?.contentResolver?.openInputStream(data.data!!)
                bitmap = BitmapFactory.decodeStream(inputStream)
                imageView.setImageBitmap(bitmap)
            } else if (requestCode == TAKE_PHOTO) {
                bitmap = data.extras?.get("data") as Bitmap
                imageView.setImageBitmap(bitmap)
            }
        }
    }

    private fun sendInputDataToBackend(nik: String, babyName: String, age: Int, weight: Float, bitmap: Bitmap) {
        val client = OkHttpClient()

        // Convert the bitmap to a JPEG byte array
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageBytes = byteArrayOutputStream.toByteArray()

        // Create a RequestBody for the image
        val imageRequestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), imageBytes)

        // Build MultipartBody with image and other form data
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("nik", nik)
            .addFormDataPart("babyName", babyName)
            .addFormDataPart("age", age.toString())
            .addFormDataPart("weight", weight.toString())
            .addFormDataPart("image", "image.jpg", imageRequestBody)
            .build()

        auth.currentUser?.getIdToken(true)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val idToken = task.result?.token
                idToken?.let { token ->
                    // Prepare the request
                    val request = Request.Builder()
                        .url("http://34.128.99.253:3000/nurse/predictions")
                        .addHeader("Authorization", "Bearer $token")
                        .post(requestBody)
                        .build()

                    // Execute the request asynchronously
                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            Log.e("InputDataFragment", "Failed to execute request", e)
                            activity?.runOnUiThread {
                                Toast.makeText(
                                    requireContext(),
                                    "Failed to upload data. Please try again.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onResponse(call: Call, response: Response) {
                            val body = response.body?.string()
                            Log.d("InputDataFragment", "Response body: $body")  // Log respons untuk verifikasi
                            if (response.isSuccessful && body != null) {
                                // Pastikan respons JSON valid
                                try {
                                    val jsonObject = JSONObject(body)
                                    val data = jsonObject.getJSONObject("data")

                                    // Extract individual fields from JSON response
                                    val chestSize = data.getDouble("lingkar_dada")
                                    val headCircumference = data.getDouble("lingkar_kepala")
                                    val armCircumference = data.getDouble("lingkar_lengan")
                                    val thighCircumference = data.getDouble("lingkar_paha")
                                    val abdominalCircumference = data.getDouble("lingkar_perut")
                                    val height = data.getDouble("panjang_badan")
                                    val category = data.getString("prediction")
                                    val confidence = data.getDouble("confidence")
                                    val suggestion = data.getString("suggestion")

                                    // Simpan data ke Firestore
                                    val db = FirebaseFirestore.getInstance()
                                    val inputData = hashMapOf(
                                        "nik" to nik,
                                        "babyName" to babyName,
                                        "age" to age,
                                        "weight" to weight,
                                        "lingkar_dada" to chestSize,
                                        "lingkar_kepala" to headCircumference,
                                        "lingkar_lengan" to armCircumference,
                                        "lingkar_paha" to thighCircumference,
                                        "lingkar_perut" to abdominalCircumference,
                                        "panjang_badan" to height,
                                        "prediction" to category,
                                        "confidence" to confidence,
                                        "suggestion" to suggestion
                                    )
                                    db.collection("predictions")
                                        .document(nik) // menggunakan 'nik' sebagai id dokumen
                                        .set(inputData)
                                        .addOnSuccessListener {
                                            Log.d(
                                                "InputDataFragment",
                                                "DocumentSnapshot successfully written!"
                                            )
                                        }
                                        .addOnFailureListener { e ->
                                            Log.w("InputDataFragment", "Error writing document", e)
                                        }

                                    // Buat instance dari ResultFragment
                                    val resultFragment = ResultFragment().apply {
                                        arguments = Bundle().apply {
                                            putString("nik", nik)
                                        }
                                    }

                                    // Replace the current fragment with the ResultFragment
                                    parentFragmentManager.beginTransaction().apply {
                                        replace(R.id.fragmentContainer, resultFragment)
                                        addToBackStack(null)
                                        commit()
                                    }

                                } catch (e: JSONException) {
                                    Log.e("InputDataFragment", "Failed to parse JSON", e)
                                    activity?.runOnUiThread {
                                        Toast.makeText(requireContext(), "Failed to parse response", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                Log.e("InputDataFragment", "Unsuccessful response: $body")
                                activity?.runOnUiThread {
                                    Toast.makeText(requireContext(), "Failed to upload data. Please try again.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    })
                } ?: run {
                    Log.e("InputDataFragment", "Token is null")
                    Toast.makeText(
                        requireContext(),
                        "Failed to authenticate user. Please log in again.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Log.e("InputDataFragment", "Failed to get ID token", task.exception)
                Toast.makeText(
                    requireContext(),
                    "Failed to authenticate user. Please log in again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
