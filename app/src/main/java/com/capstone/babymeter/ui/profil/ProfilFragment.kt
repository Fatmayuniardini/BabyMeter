package com.capstone.babymeter.ui.profil

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.capstone.babymeter.R
import com.capstone.babymeter.ui.splash.SplashActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.io.ByteArrayOutputStream

class ProfilFragment : Fragment() {

    private val PICK_IMAGE_REQUEST = 1
    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null
    private lateinit var ivProfilePicture: ImageView

    private val REQUEST_CAMERA = 1
    private val REQUEST_GALLERY = 2
    private val REQUEST_LOGOUT = 3

    private lateinit var viewModel: ProfileViewModel

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

        // Inisialisasi ViewModel
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        // Find the Logout button and set an OnClickListener
        val logoutButton: Button = view.findViewById(R.id.Logout)
        logoutButton.setOnClickListener {
            viewModel.logout()
            // Hapus status login dari SharedPreferences
            val sharedPrefs = requireActivity().getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
            sharedPrefs.edit().putBoolean("isLoggedIn", false).apply()

            // Mulai aktivitas SplashScreen
            val intent = Intent(activity, SplashActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        // Load profile picture from Shared Preferences
        val sharedPreferences = activity?.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val base64String = sharedPreferences?.getString("ProfilePicture", "")
        if (base64String != "") {
            val byteArray = Base64.decode(base64String, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            ivProfilePicture.setImageBitmap(bitmap)
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
                    saveProfilePicture(circularBitmap)
                }

                REQUEST_GALLERY -> {
                    val selectedImage: Uri? = data?.data
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        selectedImage
                    )
                    val circularBitmap = getCircularBitmap(bitmap)
                    ivProfilePicture.setImageBitmap(circularBitmap)
                    saveProfilePicture(circularBitmap)
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

    private fun saveProfilePicture(bitmap: Bitmap) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val base64String = Base64.encodeToString(byteArray, Base64.DEFAULT)

        val sharedPreferences = activity?.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putString("ProfilePicture", base64String)
        editor?.apply()
    }
}