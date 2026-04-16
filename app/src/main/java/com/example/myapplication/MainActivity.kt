package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

// FIX 1: Removed enableEdgeToEdge() — causes crash without proper setup
// FIX 2: setContentView now uses R.layout.activity_main (the actual login XML file)
// FIX 3: Removed ViewCompat insets listener (paired with enableEdgeToEdge, caused crash)
// FIX 4: forgotPassword click now navigates to ForgotPasswordActivity via Intent
// FIX 5: registerLink click now navigates to RegisterActivity via Intent
// FIX 6: Login navigates to HomeActivity and passes user_email correctly

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
                    val intent = Intent(this, HomeActivity::class.java).apply {
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