package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnClose = findViewById<ImageView>(R.id.btnClose)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val forgotPassword = findViewById<TextView>(R.id.forgotPasswordLink)
        val registerLink = findViewById<TextView>(R.id.registerLink)

        btnClose.setOnClickListener {
            finish()
        }

        btnLogin.setOnClickListener {
            val email = findViewById<TextInputEditText>(R.id.etEmail).text.toString()
            val password = findViewById<TextInputEditText>(R.id.etPassword).text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                // ✅ ADDED: Navigate to Dashboard (HomeActivity)
                val intent = Intent(this, HomeActivity::class.java).apply {
                    putExtra("user_email", email)
                }
                startActivity(intent)
                finish() // Close login screen
            }
        }

        forgotPassword.setOnClickListener {
            // TODO: Navigate to ForgotPasswordActivity
            Toast.makeText(this, "Forgot Password tapped", Toast.LENGTH_SHORT).show()
        }

        registerLink.setOnClickListener {
            // TODO: Navigate to RegisterActivity
            Toast.makeText(this, "Register tapped", Toast.LENGTH_SHORT).show()
        }
    }
}