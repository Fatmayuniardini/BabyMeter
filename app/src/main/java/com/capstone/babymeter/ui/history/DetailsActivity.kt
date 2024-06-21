package com.capstone.babymeter.ui.history

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.capstone.babymeter.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class DetailsActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        auth = FirebaseAuth.getInstance()

        val nik = intent.getStringExtra("nik") ?: ""

        fetchDetailData(nik)

        val deleteButton = findViewById<Button>(R.id.btn_delete)

        // Set an OnClickListener for the button
        deleteButton.setOnClickListener {
            // Get a reference to the document you want to delete
            val docRef = db.collection("predictions").document(nik)

            // Delete the document
            docRef.delete().addOnSuccessListener {
                // Document was successfully deleted, now update your UI here
                // For example, you could start a new Activity or update a RecyclerView
                Toast.makeText(this, "Document successfully deleted!", Toast.LENGTH_SHORT).show()
                finish() // close this activity
            }.addOnFailureListener { e ->
                // Document was not deleted, handle the error here
                Log.w(TAG, "Error deleting document", e)
                Toast.makeText(this, "Error deleting document", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun fetchDetailData(nik: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val document = db.collection("predictions").document(nik).get().await()
                if (document != null && document.exists()) {
                    val babyName = document.getString("babyName") ?: "N/A"
                    val age = document.getLong("age")?.toInt() ?: 0
                    val weight = document.getDouble("weight") ?: Double.NaN
                    val chestSize = document.getDouble("lingkar_dada") ?: Double.NaN
                    val headCircumference = document.getDouble("lingkar_kepala") ?: Double.NaN
                    val armCircumference = document.getDouble("lingkar_lengan") ?: Double.NaN
                    val thighCircumference = document.getDouble("lingkar_paha") ?: Double.NaN
                    val abdominalCircumference = document.getDouble("lingkar_perut") ?: Double.NaN
                    val height = document.getDouble("panjang_badan") ?: Double.NaN
                    val category = document.getString("prediction") ?: "N/A"

                    CoroutineScope(Dispatchers.Main).launch {
                        findViewById<TextView>(R.id.tv_detail_name).text = babyName
                        findViewById<TextView>(R.id.NIK).text = nik
                        findViewById<TextView>(R.id.Beratbayi).text = weight.toString()
                        findViewById<TextView>(R.id.lingkarkepala).text = "Head Circumference = $headCircumference"
                        findViewById<TextView>(R.id.lingkarbadan).text = "Chest Size = $chestSize"
                        findViewById<TextView>(R.id.lingkarkaki).text = "Thigh Circumference = $thighCircumference"
                        findViewById<TextView>(R.id.tinggibadan).text = "Height = $height"
                        findViewById<TextView>(R.id.bmi).text = "Abdominal Circumference = $abdominalCircumference"
                        findViewById<TextView>(R.id.categori).text = "category = $category"
                        findViewById<ImageView>(R.id.iv_detail_photo).setImageResource(R.drawable.baby)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}