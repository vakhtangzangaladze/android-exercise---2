package com.btu.vakhtang.zangaladze

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etRepeatPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var progress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        setOnTextChangeListeners()
        setOnClickListeners()
    }

    private fun init() {
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etRepeatPassword = findViewById(R.id.etRepeatPassword)
        btnRegister = findViewById(R.id.btnRegister)
        progress = findViewById(R.id.progress)

        btnRegister.isEnabled = false
    }

    private fun setOnClickListeners() {
        btnRegister.setOnClickListener { registerOnFirebase() }
    }

    private fun setOnTextChangeListeners() {
        etEmail.doOnTextChanged { _, _, _, _ -> validate() }
        etPassword.doOnTextChanged { _, _, _, _ -> validate() }
        etRepeatPassword.doOnTextChanged { _, _, _, _ -> validate() }
    }

    private fun validate() {
        val email: String = etEmail.text.toString()
        val password: String = etPassword.text.toString()
        val repeatPassword: String = etRepeatPassword.text.toString()

        val isEmailValid: Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordValid: Boolean = password.length >= 8
        val isRepeatPasswordValid: Boolean = password == repeatPassword

        btnRegister.isEnabled = isEmailValid && isPasswordValid && isRepeatPasswordValid
    }

    private fun registerOnFirebase() {
        progress.visibility = View.VISIBLE
        val email: String = etEmail.text.toString()
        val password: String = etPassword.text.toString()

        Firebase.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            progress.visibility = View.GONE

            if (it.isSuccessful)
                Toast.makeText(this, "User Register Success", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
        }
    }
}