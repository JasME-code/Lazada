package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class CartActivity : AppCompatActivity() {

    private lateinit var tvTotal: TextView
    private lateinit var listView: ListView
    private lateinit var tvSubtotal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activitycart)

        // 1. Setup Views
        tvTotal = findViewById(R.id.tvCartTotal)
        tvSubtotal = findViewById(R.id.tvCartSubtotal)
        listView = findViewById(R.id.lvCartItems)

        // 2. Setup ListView (Using the adapter from your Dashboard)
        // We use CartManager.cartList so it shows the items you added
        val adapter = Dashboard.ProductAdapter(this, CartManager.cartList)
        listView.adapter = adapter

        updateTotals()

        // 3. Back Button logic
        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish() // Goes back to the Dashboard
        }

        // 4. Checkout Button logic
        findViewById<Button>(R.id.btnCheckout).setOnClickListener {
            if (CartManager.cartList.isEmpty()) {
                Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, CheckoutActivity::class.java).apply {
                    // Send the list to the next screen
                    putParcelableArrayListExtra("SELECTED_PRODUCTS", CartManager.cartList)
                }
                startActivity(intent)
            }
        }
    }

    // Helper function to calculate the total price
    private fun updateTotals() {
        val totalAmount = CartManager.cartList.sumOf { it.price }
        val itemCount = CartManager.cartList.size

        tvTotal.text = "₱ ${"%.2f".format(totalAmount)}"
        tvSubtotal.text = "$itemCount Items"
    }

    // Refresh the list if the user comes back from another screen
    override fun onResume() {
        super.onResume()
        (listView.adapter as? BaseAdapter)?.notifyDataSetChanged()
        updateTotals()
    }
}