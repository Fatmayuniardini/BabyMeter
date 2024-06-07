package com.capstone.babymeter

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.babymeter.databinding.ActivityRegisterBinding
import com.capstone.babymeter.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    lateinit var editUsername: EditText
    lateinit var editNik: EditText
    lateinit var editEmail: EditText
    lateinit var editPassword: EditText
    lateinit var editConfirmPassword: EditText
    lateinit var btnRegister: Button
    lateinit var tvHaveAccount: Button
    lateinit var progressDialog: ProgressDialog


    var firebaseAuth = FirebaseAuth.getInstance()

    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser!=null)
            startActivity(Intent(this, MainActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.haveAnAccount.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Logging")
        progressDialog.setMessage("Silahkan tunggu...")

        binding.registerButton.setOnClickListener {
            if(editUsername.text.isNotEmpty() && editNik.text.isNotEmpty() && editEmail.text.isNotEmpty() && editPassword.text.isNotEmpty()){
                if (editPassword.text.toString() == editConfirmPassword.text.toString()){
                    //LAUNCH REGISTER
                    processRegister()
                }else{
                    Toast.makeText(this, "Konfirmasi kata sandi harus sama!", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Silahkan isi semua data!", Toast.LENGTH_SHORT).show()
            }
                startActivity(Intent(this, LoginActivity::class.java))
        }
    }
    private fun processRegister() {
        val username = editUsername.text.toString()
        val nik = editNik.text.toString()
        val email = editEmail.text.toString()
        val password = editPassword.text.toString()

        progressDialog.show()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {task->
                if (task.isSuccessful){
                    val userUpdateProfile = userProfileChangeRequest {
                        displayName = username
                        displayName = nik
                    }
                    val user = task.result.user
                    user!!.updateProfile(userUpdateProfile)
                        .addOnCompleteListener {
                            progressDialog.dismiss()
                            startActivity(Intent(this, MainActivity::class.java))
                        }
                        .addOnFailureListener { error2 ->
                            Toast.makeText(this,error2.localizedMessage, Toast.LENGTH_SHORT).show()
                        }
                }else{
                    progressDialog.dismiss()
                }
            }
            .addOnFailureListener {error ->
                Toast.makeText(this, error.localizedMessage, Toast.LENGTH_SHORT).show()

            }
    }
}


