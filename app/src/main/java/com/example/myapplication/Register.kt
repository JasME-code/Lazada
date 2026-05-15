package com.example.myapplication

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private var calculatedAge: Int = 0
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize Views
        val etName = findViewById<TextInputEditText>(R.id.etName)
        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val etDOB = findViewById<TextInputEditText>(R.id.etDOB)
        val switchAge = findViewById<SwitchMaterial>(R.id.switchAgeConfirmation)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val tvAgeDisplay = findViewById<TextView>(R.id.tvAge)
        val rbSeller = findViewById<RadioButton>(R.id.rbSeller)

        btnBack.setOnClickListener { finish() }

        // Date of Birth Picker Logic
        etDOB.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(this, { _, y, m, d ->
                val birth = Calendar.getInstance().apply { set(y, m, d) }
                etDOB.setText(SimpleDateFormat("MM/dd/yyyy", Locale.US).format(birth.time))

                val today = Calendar.getInstance()
                calculatedAge = today.get(Calendar.YEAR) - y
                if (today.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) {
                    calculatedAge--
                }
                tvAgeDisplay.text = "Age: $calculatedAge"
            }, cal.get(Calendar.YEAR) - 20, cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        btnRegister.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val role = if (rbSeller.isChecked) "Seller" else "Customer"

            // 1. Validation
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!switchAge.isChecked) {
                Toast.makeText(this, "Please confirm age status", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 2. Create User in Firebase Auth
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { authResult ->
                    // 'result' here is the AuthResult object
                    val uid = authResult.user?.uid

                    // 3. Save User Role to Database
                    val userProfile = hashMapOf(
                        "uid" to uid,
                        "name" to name,
                        "email" to email,
                        "age" to calculatedAge,
                        "role" to role
                    )

                    if (uid != null) {
                        FirebaseDatabase.getInstance().getReference("users")
                            .child(uid)
                            .setValue(userProfile)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Account Created as $role", Toast.LENGTH_SHORT).show()
                                navigateToDashboard(role)
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Database Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .addOnFailureListener { authError ->
                    // This handles errors like "Email already in use" or "No internet"
                    Toast.makeText(this, "Auth Error: ${authError.message}", Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun navigateToDashboard(role: String) {
        val intent = if (role == "Seller") {
            Intent(this, SellerDashboardActivity::class.java)
        } else {
            Intent(this, Dashboard::class.java)
        }
        startActivity(intent)
        finish()
    }
}