package com.capstone.babymeter.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.capstone.babymeter.MainActivity
import com.capstone.babymeter.R
import com.capstone.babymeter.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check if user is already logged in
        val sharedPrefs = getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPrefs.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            // Directly go to MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            authViewModel.login(email, password)
        }

        // Handle click on "Register" part of the TextView
        val spannableString = SpannableString(getString(R.string.login_havent_account))
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
        spannableString.setSpan(clickableSpan, spannableString.indexOf("Register"),
            spannableString.indexOf("Register") + "Register".length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Set TextView text and make "Register" clickable
        binding.tvHaventAccount.text = spannableString
        binding.tvHaventAccount.movementMethod = LinkMovementMethod.getInstance()

        authViewModel.authState.observe(this, Observer { authState ->
            when (authState) {
                is AuthState.Idle -> {
                    // Initial state or after logout
                    binding.progressBar.visibility = View.GONE
                }
                is AuthState.Loading -> {
                    // Show loading indicator
                    binding.progressBar.visibility = View.VISIBLE
                }
                is AuthState.Success -> {
                    // Handle successful login
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()

                    // Save login status in SharedPreferences
                    sharedPrefs.edit().putBoolean("isLoggedIn", true).apply()

                    // Navigate to MainActivity
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Finish LoginActivity to prevent returning to it with back button
                }
                is AuthState.Error -> {
                    // Handle failed login
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Login Failed: ${authState.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
