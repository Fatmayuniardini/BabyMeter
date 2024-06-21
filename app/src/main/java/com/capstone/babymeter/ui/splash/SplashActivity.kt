package com.capstone.babymeter.ui.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.capstone.babymeter.R
import com.capstone.babymeter.ui.login.LoginActivity
import com.capstone.babymeter.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        auth = FirebaseAuth.getInstance()

        // Check if user is signed in
        val currentUser = auth.currentUser
        val sharedPrefs = getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPrefs.getBoolean("isLoggedIn", false)

        val onboardingPrefs = getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        val isOnboardingCompleted = onboardingPrefs.getBoolean("isOnboardingCompleted", false)

        // Add delay using Handler
        Handler(Looper.getMainLooper()).postDelayed({
            if (currentUser != null && isLoggedIn) {
                // User is signed in, navigate to the MainActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else if (!isLoggedIn && !isOnboardingCompleted) {
                // User is not signed in and has not completed onboarding, navigate to the OnboardingActivity
                val intent = Intent(this, OnboardingActivity::class.java)
                startActivity(intent)
            } else {
                // User is not signed in but has completed onboarding, navigate to the LoginActivity
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }

            // Finish this activity so user can't navigate back to it
            finish()
        }, 3000) // Delay for 3 seconds
    }
}