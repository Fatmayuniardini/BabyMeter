package com.capstone.babymeter.ui.register

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
import com.capstone.babymeter.R
import com.capstone.babymeter.auth.AuthState
import com.capstone.babymeter.auth.AuthViewModel
import com.capstone.babymeter.databinding.ActivityRegisterBinding
import com.capstone.babymeter.model.request.RegisterRequest
import com.capstone.babymeter.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val name = binding.nameEditText.text.toString()
            val registerRequest = RegisterRequest(email, password, name)
            authViewModel.register(registerRequest)
        }

        // Handle click on "Register" part of the TextView
        val spannableString = SpannableString(getString(R.string.have_an_account))
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }
        spannableString.setSpan(clickableSpan, spannableString.indexOf("Login"),
            spannableString.indexOf("Login") + "Login".length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Set TextView text and make "Register" clickable
        binding.haveAnAccount.text = spannableString
        binding.haveAnAccount.movementMethod = LinkMovementMethod.getInstance()

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
                    // Handle successful registration
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                    // Navigate to the next screen or login screen
                }
                is AuthState.Error -> {
                    // Handle failed registration
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Registration Failed: ${authState.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
