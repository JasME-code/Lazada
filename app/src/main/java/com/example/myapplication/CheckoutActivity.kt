package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CheckoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activitycheckout)

        // 1. GET DATA: Retrieve the list of products from the Intent
        val selectedProducts = intent.getParcelableArrayListExtra<Product>("SELECTED_PRODUCTS") ?: arrayListOf()

        // 2. SETUP VIEWS
        val lvCheckoutItems = findViewById<ListView>(R.id.lvCheckoutItems)
        val tvTotalAmount   = findViewById<TextView>(R.id.tvOrderTotal)
        val tvItemQty       = findViewById<TextView>(R.id.tvOrderQty)
        val btnPlaceOrder   = findViewById<Button>(R.id.btnPlaceOrder)
        val btnBack         = findViewById<ImageView>(R.id.btnBack)

        // 3. ATTACH ADAPTER: Use the same ProductAdapter from Dashboard
        // This displays all items you added in the Cart
        val adapter = Dashboard.ProductAdapter(this, selectedProducts)
        lvCheckoutItems.adapter = adapter

        // 4. CALCULATE TOTALS
        val totalAmount = selectedProducts.sumOf { it.price }
        val itemCount   = selectedProducts.size

        tvTotalAmount.text = "₱ ${"%.2f".format(totalAmount)}"
        tvItemQty.text     = "Total ($itemCount items)"

        // 5. BACK BUTTON: Returns to Cart
        btnBack.setOnClickListener {
            finish()
        }

        // 6. PLACE ORDER BUTTON
        btnPlaceOrder.setOnClickListener {
            if (selectedProducts.isEmpty()) {
                Toast.makeText(this, "No items to order!", Toast.LENGTH_SHORT).show()
            } else {
                // Success Message
                Toast.makeText(this, "Order Placed Successfully! 🎉", Toast.LENGTH_LONG).show()

                // CLEAR THE GLOBAL CART: After buying, the cart should be empty
                CartManager.cartList.clear()

                // RETURN TO DASHBOARD: Clear activity stack so user can't "back" into checkout
                val intent = Intent(this, Dashboard::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }
    }
}