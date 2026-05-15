package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
// --- FIREBASE IMPORTS ---
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // 2. SESSION CHECK (The true missing part)
        // If user is already logged in, skip login screen and check role immediately
        val currentUser = auth.currentUser
        if (currentUser != null) {
            checkUserRole(currentUser.uid)
        }

        // Initialize Views
        val btnClose       = findViewById<ImageView>(R.id.btnClose)
        val btnLogin       = findViewById<Button>(R.id.btnLogin)
        val forgotPassword = findViewById<TextView>(R.id.forgotPasswordLink)
        val registerLink   = findViewById<TextView>(R.id.registerLink)
        val etEmail        = findViewById<TextInputEditText>(R.id.etEmail)
        val etPassword     = findViewById<TextInputEditText>(R.id.etPassword)

        btnClose.setOnClickListener { finish() }

        btnLogin.setOnClickListener {
            val email    = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 3. AUTHENTICATION LOGIC
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { authResult ->
                    val uid = authResult.user?.uid
                    if (uid != null) {
                        checkUserRole(uid)
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Login Failed: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }

        forgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    /**
     * THE ROUTER: This detects if the user is a Seller or Customer
     * based on the 'role' field in Firebase Realtime Database.
     */
    private fun checkUserRole(uid: String) {
        val dbRef = FirebaseDatabase.getInstance().getReference("users").child(uid)

        dbRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val role = snapshot.child("role").getValue(String::class.java)

                if (role == "Seller") {
                    // Go to Seller Dashboard
                    startActivity(Intent(this, SellerDashboardActivity::class.java))
                } else {
                    // Go to Customer Dashboard
                    startActivity(Intent(this, Dashboard::class.java))
                }
                finish() // Finish MainActivity so user can't go 'back' to Login
            } else {
                Toast.makeText(this, "User record missing in Database.", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { e ->
            Toast.makeText(this, "Database error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}