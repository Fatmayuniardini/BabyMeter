package com.example.onboardingpage

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.capstone.babymeter.R
import com.capstone.babymeter.databinding.ActivityOnboardingBinding
import com.capstone.babymeter.fragments.HomeFragment

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonNext.setOnClickListener {
            val intent = Intent(this@OnboardingActivity, HomeFragment::class.java)
            startActivity(intent)
            finish() // Tutup OnboardingActivity
        }
    }
}
