package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import android.widget.BaseAdapter
import androidx.appcompat.app.AppCompatActivity

class CartActivity : AppCompatActivity() {

    private lateinit var tvTotal: TextView
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activitycart)

        // 1. Setup Views
        tvTotal = findViewById(R.id.tvCartTotal)
        listView = findViewById(R.id.lvCartItems) // Ensure this ID is in your XML

        // 2. Setup ListView with our Global Cart
        val adapter = Dashboard.ProductAdapter(this, CartManager.cartList)
        listView.adapter = adapter

        updateTotals()

        // Back button
        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // Proceed to Checkout
        findViewById<Button>(R.id.btnCheckout).setOnClickListener {
            if (CartManager.cartList.isEmpty()) {
                Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, CheckoutActivity::class.java).apply {
                // Pass the whole list to Checkout
                putParcelableArrayListExtra("SELECTED_PRODUCTS", CartManager.cartList)
            }
            startActivity(intent)
        }
    }

    private fun updateTotals() {
        val totalAmount = CartManager.cartList.sumOf { it.price }
        tvTotal.text = "₱ ${"%.2f".format(totalAmount)}"

        findViewById<TextView>(R.id.tvCartSubtotal)?.text = "${CartManager.cartList.size} Items"
    }

    override fun onResume() {
        super.onResume()
        (listView.adapter as? BaseAdapter)?.notifyDataSetChanged()
        updateTotals()
    }
}