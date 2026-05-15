package com.example.myapplication

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
// --- FIREBASE IMPORTS ---
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EditListingsActivity : AppCompatActivity() {

    private val sellerProducts = mutableListOf<Product>()
    private lateinit var adapter: Dashboard.ProductAdapter
    private val database = FirebaseDatabase.getInstance().getReference("products")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_listings)

        // 1. SETUP VIEWS
        val lvEditListings = findViewById<ListView>(R.id.lvEditListings)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        btnBack.setOnClickListener { finish() }

        // 2. SETUP ADAPTER
        // We use our local sellerProducts list which will be filled by Firebase
        adapter = Dashboard.ProductAdapter(this, sellerProducts)
        lvEditListings.adapter = adapter

        // 3. FETCH FROM FIREBASE
        fetchSellerProducts()

        // 4. ITEM CLICK (Example: Delete on click)
        lvEditListings.setOnItemClickListener { _, _, position, _ ->
            val product = sellerProducts[position]

            // Example Logic: Show a dialog to Delete
            val builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("Manage Product")
            builder.setMessage("Do you want to delete ${product.name}?")
            builder.setPositiveButton("Delete") { _, _ ->
                database.child(product.id.toString()).removeValue()
                Toast.makeText(this, "Product Removed", Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("Cancel", null)
            builder.show()
        }
    }

    private fun fetchSellerProducts() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                sellerProducts.clear()
                for (postSnapshot in snapshot.children) {
                    val product = postSnapshot.getValue(Product::class.java)
                    if (product != null) {
                        // Optional: Filter only products belonging to "My Local Shop"
                        // if (product.seller == "My Local Shop") {
                        sellerProducts.add(product)
                        // }
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@EditListingsActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}