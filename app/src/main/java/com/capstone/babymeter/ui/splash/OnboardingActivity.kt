package com.capstone.babymeter.ui.splash

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.capstone.babymeter.R
import com.capstone.babymeter.ui.login.LoginActivity

class OnboardingActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        sharedPreferences = getSharedPreferences("MyApp", Context.MODE_PRIVATE)

        // Assume this button is the last step of your onboarding
        val finishButton: Button = findViewById(R.id.buttonNext)
        finishButton.setOnClickListener {
            // When user finishes onboarding, save the status to SharedPreferences
            sharedPreferences.edit().putBoolean("hasOnboarded", true).apply()

            // Then navigate to LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

            // Finish this activity so user can't navigate back to it
            finish()
        }
    }
}