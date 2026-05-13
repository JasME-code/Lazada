package com.example.myapplication

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AddProductActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        // 1. SETUP VIEWS
        val btnSubmit = findViewById<Button>(R.id.btnSubmitProduct)
        val etName = findViewById<EditText>(R.id.etProductName)
        val etPrice = findViewById<EditText>(R.id.etProductPrice)
        val etDesc = findViewById<EditText>(R.id.etProductDesc)

        // 2. BUTTON CLICK LOGIC
        btnSubmit.setOnClickListener {
            val name = etName.text.toString().trim()
            val priceStr = etPrice.text.toString().trim()
            val desc = etDesc.text.toString().trim()

            if (name.isNotEmpty() && priceStr.isNotEmpty()) {
                val price = priceStr.toDoubleOrNull() ?: 0.0

                // --- FIXED PRODUCT OBJECT ---
                // Added 'specs' to match your data class exactly
                val newProduct = Product(
                    id = (Dashboard.productList.size + 1),
                    name = name,
                    price = price,
                    rating = 5.0f,
                    seller = "My Local Shop",
                    imageRes = android.R.drawable.ic_menu_gallery,
                    category = "General",
                    stock = 10,
                    specs = desc,             // <--- THIS WAS THE MISSING PART
                    description = desc,
                    reviews = listOf("New product arrival!"),
                    isRestricted = false
                )

                // 3. PUSH TO GLOBAL LIST
                Dashboard.productList.add(newProduct)

                Toast.makeText(this, "Success: $name is now listed!", Toast.LENGTH_SHORT).show()

                // Return to previous screen
                finish()
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}