package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

// FIX 1: setContentView now uses R.layout.forgotpassword (the actual file is forgotpassword.xml)
// FIX 2: Removed navigation to VerificationActivity (doesn't exist) — shows confirmation Toast
//         and goes back to Login (MainActivity) instead

class ForgotPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgotpassword)  // FIX: was R.layout.activity_forgot_password (file doesn't exist)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        val btnBack      = findViewById<ImageView>(R.id.btnBack)
        val btnSendCode  = findViewById<Button>(R.id.btnSendCode)
        val etResetEmail = findViewById<TextInputEditText>(R.id.etResetEmail)
        val tvSupport    = findViewById<TextView>(R.id.tvSupport)

        btnBack.setOnClickListener {
            finish()  // Back to Login
        }

        btnSendCode.setOnClickListener {
            val emailOrPhone = etResetEmail.text.toString().trim()

            when {
                emailOrPhone.isEmpty() -> {
                    Toast.makeText(this, "Please enter email or phone number", Toast.LENGTH_SHORT).show()
                }
                !isValidEmail(emailOrPhone) && !isValidPhone(emailOrPhone) -> {
                    Toast.makeText(this, "Please enter a valid email or phone number", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this, "Verification code sent to $emailOrPhone 📧", Toast.LENGTH_LONG).show()

                    // FIX: VerificationActivity doesn't exist — navigate back to Login instead
                    val intent = Intent(this, MainActivity::class.java).apply {
                        putExtra("reset_identifier", emailOrPhone)
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    }
                    startActivity(intent)
                    finish()
                }
            }
        }

        tvSupport.setOnClickListener {
            Toast.makeText(this, "Contacting Customer Support... 📞", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPhone(phone: String): Boolean {
        return phone.length >= 10 && phone.matches(Regex("^[0-9+\\-\\s()]+$"))
    }
}