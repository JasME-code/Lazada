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

        // 1. RECEIVE THE LIST OF PRODUCTS
        // "SELECTED_PRODUCTS" is the key we used when sending from Dashboard
        val productList = intent.getParcelableArrayListExtra<Product>("SELECTED_PRODUCTS") ?: arrayListOf()

        // 2. SETUP THE LISTVIEW
        val listView = findViewById<ListView>(R.id.lvCheckoutItems)
        // Reuse your existing ProductAdapter!
        val adapter = ProductAdapter(this, productList)
        listView.adapter = adapter

        // 3. CALCULATE TOTALS
        val totalAmount = productList.sumOf { it.price }
        val totalQty = productList.size

        // 4. BIND TO SUMMARY VIEWS
        findViewById<TextView>(R.id.tvOrderQty).text = "Items: $totalQty"
        findViewById<TextView>(R.id.tvOrderTotal).text = "Total: ₱ ${"%.2f".format(totalAmount)}"


        // Back button
        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // Place Order
        findViewById<Button>(R.id.btnPlaceOrder).setOnClickListener {
            if (productList.isEmpty()) {
                Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val orderId = "ORD-${System.currentTimeMillis()}"
            Toast.makeText(this, "Order for $totalQty items placed! 🎉", Toast.LENGTH_LONG).show()

            // Return to Dashboard and clear stack
            val intent = Intent(this, Dashboard::class.java).apply {
                putExtra("order_id", orderId)
                putExtra("order_total", totalAmount)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
            finish()
        }
    }
}