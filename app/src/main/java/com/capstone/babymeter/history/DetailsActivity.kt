package com.capstone.babymeter.history

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.capstone.babymeter.R

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val item = intent.getSerializableExtra("item") as? HistoryItem

        val name: TextView = findViewById(R.id.tv_detail_name)
        val nik: TextView = findViewById(R.id.NIK) // Ganti dengan ID yang sesuai di layout Anda
        val beratBayi: TextView = findViewById(R.id.Beratbayi) // Ganti dengan ID yang sesuai di layout Anda
        val lingkarKepala: TextView = findViewById(R.id.lingkarkepala) // Ganti dengan ID yang sesuai di layout Anda
        val lingkarBadan: TextView = findViewById(R.id.lingkarbadan) // Ganti dengan ID yang sesuai di layout Anda
        val lingkarKaki: TextView = findViewById(R.id.lingkarkaki) // Ganti dengan ID yang sesuai di layout Anda
        val tinggiBadan: TextView = findViewById(R.id.tinggibadan) // Ganti dengan ID yang sesuai di layout Anda
        val bmi: TextView = findViewById(R.id.bmi) // Ganti dengan ID yang sesuai di layout Anda
        val category: TextView = findViewById(R.id.categori) // Ganti dengan ID yang sesuai di layout Anda
        val photo: ImageView = findViewById(R.id.iv_detail_photo)

        name.text = item?.name
        nik.text = item?.nik
        beratBayi.text = item?.beratBayi
        lingkarKepala.text = item?.lingkarKepala
        lingkarBadan.text = item?.lingkarBadan
        lingkarKaki.text = item?.lingkarKaki
        tinggiBadan.text = item?.tinggiBadan
        bmi.text = item?.bmi
        category.text = item?.category

        // Load image with Glide or any other image loading library
        Glide.with(this).load(item?.imageUrl).into(photo)
    }
}