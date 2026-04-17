package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)  // FIX: was R.layout.activity_login (file doesn't exist)

        val btnClose       = findViewById<ImageView>(R.id.btnClose)
        val btnLogin       = findViewById<Button>(R.id.btnLogin)
        val forgotPassword = findViewById<TextView>(R.id.forgotPasswordLink)
        val registerLink   = findViewById<TextView>(R.id.registerLink)

        btnClose.setOnClickListener {
            finish()
        }

        btnLogin.setOnClickListener {
            val email    = findViewById<TextInputEditText>(R.id.etEmail).text.toString().trim()
            val password = findViewById<TextInputEditText>(R.id.etPassword).text.toString().trim()

            when {
                email.isEmpty() || password.isEmpty() -> {
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
                password.length < 6 -> {
                    Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // Navigate to Dashboard, pass user email
                    val intent = Intent(this, Dashboard::class.java).apply {
                        putExtra("user_email", email)
                    }
                    startActivity(intent)
                    finish()
                }
            }
        }

        // FIX: Was just a Toast — now actually navigates to ForgotPasswordActivity
        forgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        // FIX: Was just a Toast — now actually navigates to RegisterActivity
        registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}