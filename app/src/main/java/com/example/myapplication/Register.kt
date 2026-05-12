package com.example.myapplication

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private var calculatedAge: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)

        val etName = findViewById<TextInputEditText>(R.id.etName)
        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val etDOB = findViewById<TextInputEditText>(R.id.etDOB)
        val rgType = findViewById<RadioGroup>(R.id.rgAccountType)
        val switchAge = findViewById<SwitchMaterial>(R.id.switchAgeConfirmation)

        etDOB.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(this, { _, y, m, d ->
                val birth = Calendar.getInstance().apply { set(y, m, d) }
                etDOB.setText(SimpleDateFormat("MM/dd/yyyy", Locale.US).format(birth.time))

                val today = Calendar.getInstance()
                calculatedAge = today.get(Calendar.YEAR) - y
                if (today.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) calculatedAge--
                findViewById<TextView>(R.id.tvAge).text = "Age: $calculatedAge"
            }, 2000, 0, 1).show()
        }

        findViewById<Button>(R.id.btnRegister).setOnClickListener {
            val role =
                if (findViewById<RadioButton>(R.id.rbSeller).isChecked) "Seller" else "Customer"
            if (calculatedAge == 0) {
                Toast.makeText(this, "Please select Date of Birth", Toast.LENGTH_SHORT).show()
            } else if (!switchAge.isChecked) {
                Toast.makeText(this, "Please confirm age status", Toast.LENGTH_SHORT).show()
            } else {
                val user =
                    User(etName.text.toString(), etEmail.text.toString(), calculatedAge, role)
                val intent =
                    Intent(this, Dashboard::class.java).apply { putExtra("USER_DATA", user) }
                startActivity(intent)
                finish()
            }
        }
    }
}