package com.capstone.babymeter.ui.predictions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.capstone.babymeter.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.FirebaseFirestore
import com.capstone.babymeter.ui.history.HistoryFragment // Pastikan path ini benar

class ResultFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_result, container, false)

        val nikTextView: TextView = view.findViewById(R.id.NIK)
        val nameTextView: TextView = view.findViewById(R.id.namabayi)
        val ageTextView: TextView = view.findViewById(R.id.umurbayi)
        val weightTextView: TextView = view.findViewById(R.id.beratbayi)
        val chestTextView: TextView = view.findViewById(R.id.lingkardada)
        val headTextView: TextView = view.findViewById(R.id.lingkarkepala)
        val armTextView: TextView = view.findViewById(R.id.lingkarlengan)
        val thighTextView: TextView = view.findViewById(R.id.lingkarpaha)
        val abdominalTextView: TextView = view.findViewById(R.id.lingkarperut)
        val heightTextView: TextView = view.findViewById(R.id.tinggibadan)
        val categoryTextView: TextView = view.findViewById(R.id.categori)
        val suggestionTextView: TextView = view.findViewById(R.id.suggestion)

        // Get the nik from arguments
        val nik = arguments?.getString("nik") ?: ""

        val saveButton: Button = view.findViewById(R.id.buttonsave)

        // Call Firestore to get the prediction result
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
                    val suggestion = document.getString("suggestion") ?: "N/A"

                    CoroutineScope(Dispatchers.Main).launch {
                        nikTextView.text = "NIK: $nik"
                        nameTextView.text = "Baby Name: $babyName"
                        ageTextView.text = "Age: $age"
                        weightTextView.text = "Weight: $weight"
                        chestTextView.text = "Chest Size: $chestSize"
                        headTextView.text = "Head Circumference: $headCircumference"
                        armTextView.text = "Arm Circumference: $armCircumference"
                        thighTextView.text = "Thigh Circumference: $thighCircumference"
                        abdominalTextView.text = "Abdominal Circumference: $abdominalCircumference"
                        heightTextView.text = "Height: $height"
                        categoryTextView.text = "Category: $category"
                        suggestionTextView.text = "Suggestion: $suggestion"
                    }
                } else {
                    CoroutineScope(Dispatchers.Main).launch {
                        nikTextView.text = "Failed to get prediction result"
                    }
                }
            } catch (e: Exception) {
                CoroutineScope(Dispatchers.Main).launch {
                    nikTextView.text = "Failed to get prediction result"
                }
            }
        }

        saveButton.setOnClickListener {
            // Replace current fragment with HistoryFragment
            val historyFragment = HistoryFragment()
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainer, historyFragment)
                addToBackStack(null)
                commit()
            }
        }

        return view
    }
}
