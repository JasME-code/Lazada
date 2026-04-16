package com.example.myapplication

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial  // FIX: removed duplicate import of SwitchMaterial from android.widget
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

// FIX 1: Removed duplicate conflicting import (android.widget.SwitchMaterial + com.google.android.material.switchmaterial.SwitchMaterial)
// FIX 2: setContentView uses correct layout R.layout.signup (the actual file is signup.xml)
// FIX 3: After successful registration, navigate to MainActivity (Login) not HomeActivity
//         Registration should go back to login so user logs in with their new account

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
        setContentView(R.layout.signup)  // FIX: was R.layout.activity_register (file doesn't exist)

        initViews()
        setupClickListeners()
    }

    private fun initViews() {
        etName               = findViewById(R.id.etName)
        etEmail              = findViewById(R.id.etEmail)
        etDOB                = findViewById(R.id.etDOB)
        etPassword           = findViewById(R.id.etPassword)
        tvAge                = findViewById(R.id.tvAge)
        switchAgeConfirmation = findViewById(R.id.switchAgeConfirmation)
        btnRegister          = findViewById(R.id.btnRegister)
    }

    private fun setupClickListeners() {
        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        btnBack.setOnClickListener {
            finish()  // Back to Login
        }

        etDOB.setOnClickListener {
            showDatePicker()
        }

        btnRegister.setOnClickListener {
            performRegistration()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year  = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day   = calendar.get(Calendar.DAY_OF_MONTH)

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
            year - 20, month, day
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
            age < 18  -> "⚠️ Age: $age years (Minor - Limited access)"
            age >= 18 -> "✅ Age: $age years (Full access including 18+)"
            else      -> "Enter valid DOB"
        }

        tvAge.setBackgroundColor(
            if (age >= 18) 0xFF4CAF50.toInt() else 0xFFFF9800.toInt()
        )
    }

    private fun performRegistration() {
        val name         = etName.text.toString().trim()
        val email        = etEmail.text.toString().trim()
        val dob          = etDOB.text.toString().trim()
        val password     = etPassword.text.toString().trim()
        val ageConfirmed = switchAgeConfirmation.isChecked

        when {
            name.isEmpty() ->
                showError(findViewById(R.id.tilName), "Name is required")
            email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                showError(findViewById(R.id.tilEmail), "Valid email is required")
            dob.isEmpty() ->
                showError(findViewById(R.id.tilDOB), "Date of birth is required")
            password.length < 6 ->
                showError(findViewById(R.id.tilPassword), "Password must be 6+ characters")
            !ageConfirmed ->
                Toast.makeText(this, "Please confirm age restriction", Toast.LENGTH_SHORT).show()
            else -> {
                Toast.makeText(this, "Account created for $name! 🎉", Toast.LENGTH_LONG).show()

                // FIX: Navigate back to Login (MainActivity) after registration
                // Pass registered email so login screen can pre-fill it
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra("registered_email", email)
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                startActivity(intent)
                finish()
            }
        }
    }

    private fun showError(inputLayout: TextInputLayout, error: String) {
        inputLayout.error = error
        inputLayout.editText?.requestFocus()
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }
}