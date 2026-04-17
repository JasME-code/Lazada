package com.example.myapplication

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var etName: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etDOB: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var tvAge: TextView
    private lateinit var switchAgeConfirmation: SwitchMaterial
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // FIX: Ensure your XML file is named 'signup.xml' in res/layout
        setContentView(R.layout.signup)

        try {
            initViews()
            setupClickListeners()
        } catch (e: Exception) {
            // This helps you see in Logcat exactly which View ID is missing
            e.printStackTrace()
            Toast.makeText(this, "Error initializing views: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun initViews() {
        // IMPORTANT: These IDs MUST exist in signup.xml
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etDOB = findViewById(R.id.etDOB)
        etPassword = findViewById(R.id.etPassword)
        tvAge = findViewById(R.id.tvAge)
        switchAgeConfirmation = findViewById(R.id.switchAgeConfirmation)
        btnRegister = findViewById(R.id.btnRegister)
    }

    private fun setupClickListeners() {
        // Using a safe find for the back button
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack?.setOnClickListener {
            finish()
        }

        // Fix: Make the EditText non-editable so user MUST use the picker
        etDOB.isFocusable = false
        etDOB.isClickable = true
        etDOB.setOnClickListener {
            showDatePicker()
        }

        btnRegister.setOnClickListener {
            performRegistration()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDay)
                }
                val formattedDate = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(selectedDate.time)
                etDOB.setText(formattedDate)
                calculateAge(selectedDate)
            },
            year - 18, month, day // Default to 18 years ago
        )
        datePicker.datePicker.maxDate = System.currentTimeMillis()
        datePicker.show()
    }

    private fun calculateAge(birthDate: Calendar) {
        val today = Calendar.getInstance()
        var age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)

        if (today.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
            age--
        }

        tvAge.text = when {
            age < 18 -> "⚠️ Age: $age years (Minor)"
            else -> "✅ Age: $age years (Full Access)"
        }

        // Use a standard Android color if hex fails
        tvAge.setTextColor(if (age >= 18) android.graphics.Color.GREEN else android.graphics.Color.RED)
    }

    private fun performRegistration() {
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val dob = etDOB.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val ageConfirmed = switchAgeConfirmation.isChecked

        // Reset errors first
        findViewById<TextInputLayout>(R.id.tilName).error = null
        findViewById<TextInputLayout>(R.id.tilEmail).error = null
        findViewById<TextInputLayout>(R.id.tilDOB).error = null
        findViewById<TextInputLayout>(R.id.tilPassword).error = null

        when {
            name.isEmpty() -> showError(findViewById(R.id.tilName), "Name is required")
            email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                showError(findViewById(R.id.tilEmail), "Valid email is required")
            dob.isEmpty() -> showError(findViewById(R.id.tilDOB), "Date of birth is required")
            password.length < 6 -> showError(findViewById(R.id.tilPassword), "Password must be 6+ characters")
            !ageConfirmed -> Toast.makeText(this, "Please confirm age restriction", Toast.LENGTH_SHORT).show()
            else -> {
                Toast.makeText(this, "Welcome, $name!", Toast.LENGTH_LONG).show()
                val intent = Intent(this, Dashboard::class.java).apply {
                    putExtra("user_name", name)
                    putExtra("user_email", email)
                }
                startActivity(intent)
                finish()
            }
        }
    }

    private fun showError(inputLayout: TextInputLayout?, error: String) {
        inputLayout?.error = error
        inputLayout?.editText?.requestFocus()
    }
}