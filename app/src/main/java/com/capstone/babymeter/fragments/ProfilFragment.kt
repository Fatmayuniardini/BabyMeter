package com.capstone.babymeter.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import com.capstone.babymeter.R
import com.capstone.babymeter.SplashActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ProfilFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null
    private lateinit var ivProfilePicture: ImageView

    private val REQUEST_CAMERA = 1
    private val REQUEST_GALLERY = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profil, container, false)

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser

        ivProfilePicture = view.findViewById(R.id.ivProfilePicture)

        val profileNameTextView: TextView = view.findViewById(R.id.tvProfileName)
        val fullNameTextView: TextView = view.findViewById(R.id.Fullname)
        val passwordTextView: TextView = view.findViewById(R.id.Passwordd)

        // Set user data
        profileNameTextView.text = user?.displayName ?: "User"
        fullNameTextView.text = user?.email ?: "Email not available"
        passwordTextView.text = "********" // Display placeholder for password

        // Find the Change Photo button and set an OnClickListener
        val changePhotoButton: Button = view.findViewById(R.id.btnChangePhoto)
        changePhotoButton.setOnClickListener {
            showPhotoDialog()
        }

        // Find the Logout button and set an OnClickListener
        val logoutButton: Button = view.findViewById(R.id.Logout)
        logoutButton.setOnClickListener {
            // Sign out user
            auth.signOut()
            // Intent to navigate to SplashScreenActivity
            val intent = Intent(activity, SplashActivity::class.java)
            startActivity(intent)
            activity?.finish() // Optional: finish the current activity
        }

        return view
    }

    private fun showPhotoDialog() {
        val options = arrayOf("Camera", "Gallery")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Choose a source")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> takePhotoFromCamera()
                1 -> choosePhotoFromGallery()
            }
        }
        builder.show()
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CAMERA)
    }

    private fun choosePhotoFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CAMERA -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    val circularBitmap = getCircularBitmap(imageBitmap)
                    ivProfilePicture.setImageBitmap(circularBitmap)
                    // Simpan gambar ke Firebase Storage atau lokasi yang diinginkan
                }

                REQUEST_GALLERY -> {
                    val selectedImage: Uri? = data?.data
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        selectedImage
                    )
                    val circularBitmap = getCircularBitmap(bitmap)
                    ivProfilePicture.setImageBitmap(circularBitmap)
                    // Simpan gambar ke Firebase Storage atau lokasi yang diinginkan
                }
            }
        }
    }

    private fun getCircularBitmap(bitmap: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        canvas.drawCircle(
            (bitmap.width / 2).toFloat(),
            (bitmap.height / 2).toFloat(),
            (bitmap.width / 2).toFloat(),
            paint
        )
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        return output
    }
}