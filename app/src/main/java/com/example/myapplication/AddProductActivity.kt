package com.example.myapplication

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
// --- FIREBASE IMPORT ---
import com.google.firebase.database.FirebaseDatabase

class AddProductActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        // 1. SETUP VIEWS
        val btnSubmit = findViewById<Button>(R.id.btnSubmitProduct)
        val etName = findViewById<EditText>(R.id.etProductName)
        val etPrice = findViewById<EditText>(R.id.etProductPrice)
        val etQuantity = findViewById<EditText>(R.id.etProductQuantity)
        val etDesc = findViewById<EditText>(R.id.etProductDesc)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        // Back button simply closes the activity
        btnBack?.setOnClickListener { finish() }

        // 2. BUTTON CLICK LOGIC
        btnSubmit.setOnClickListener {
            val name = etName.text.toString().trim()
            val priceStr = etPrice.text.toString().trim()
            val qtyStr = etQuantity.text.toString().trim()
            val desc = etDesc.text.toString().trim()

            // Validate inputs
            if (name.isNotEmpty() && priceStr.isNotEmpty() && qtyStr.isNotEmpty()) {
                val price = priceStr.toDoubleOrNull() ?: 0.0
                val quantity = qtyStr.toIntOrNull() ?: 0

                // 3. GENERATE UNIQUE ID
                // Using timestamp ensures every new product has a unique ID in Firebase
                val newId = System.currentTimeMillis().toInt()

                // 4. CREATE THE PRODUCT OBJECT
                val newProduct = Product(
                    id = newId,
                    name = name,
                    price = price,
                    rating = 5.0f,
                    seller = "My Local Shop",
                    imageRes = R.drawable.img,
                    category = "General",
                    stock = quantity,
                    material = "Standard",
                    usage = "General",
                    details = listOf(desc),
                    isRestricted = false
                )

                // 5. THE CLOUD STEP: SAVE TO FIREBASE
                // This replaces the local list. It saves the data to Google's servers.
                val database = FirebaseDatabase.getInstance().getReference("products")

                database.child(newId.toString()).setValue(newProduct)
                    .addOnSuccessListener {
                        // This block runs only if the cloud successfully receives the data
                        Toast.makeText(this, "Success: $name is now live in the cloud!", Toast.LENGTH_SHORT).show()

                        // Return to Seller Dashboard
                        finish()
                    }
                    .addOnFailureListener { e ->
                        // Helpful if there is a connection issue
                        Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }

            } else {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}