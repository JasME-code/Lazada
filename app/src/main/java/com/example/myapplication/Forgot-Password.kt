package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class ForgotPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnSendCode = findViewById<Button>(R.id.btnSendCode)
        val etResetEmail = findViewById<TextInputEditText>(R.id.etResetEmail)
        val tvSupport = findViewById<TextView>(R.id.tvSupport)

        // Back button
        btnBack.setOnClickListener {
            finish() // Go back to login
        }

        // Send verification code
        btnSendCode.setOnClickListener {
            val emailOrPhone = etResetEmail.text.toString().trim()

            when {
                emailOrPhone.isEmpty() -> {
                    Toast.makeText(this, "Please enter email or phone number", Toast.LENGTH_SHORT).show()
                }
                !isValidEmail(emailOrPhone) && !isValidPhone(emailOrPhone) -> {
                    Toast.makeText(this, "Please enter valid email or phone number", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // ✅ Send verification code
                    Toast.makeText(this, "Verification code sent to $emailOrPhone 📧", Toast.LENGTH_LONG).show()

                    // Navigate to Verification screen
                    val intent = Intent(this, VerificationActivity::class.java).apply {
                        putExtra("user_identifier", emailOrPhone)
                    }
                    startActivity(intent)
                }
            }
        }

        // Customer support
        tvSupport.setOnClickListener {
            Toast.makeText(this, "Contacting Customer Support... 📞", Toast.LENGTH_SHORT).show()
            // Open support chat or call
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPhone(phone: String): Boolean {
        return phone.length >= 10 && phone.matches(Regex("^[0-9+\\-\\s()]+$"))
    }
}