package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.ArrayList

class CartActivity : AppCompatActivity() {

    private lateinit var tvTotal: TextView
    private lateinit var listView: ListView
    private lateinit var tvSubtotal: TextView
    private lateinit var adapter: Dashboard.ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activitycart)

        // 1. Setup Views
        tvTotal = findViewById(R.id.tvCartTotal)
        tvSubtotal = findViewById(R.id.tvCartSubtotal)
        listView = findViewById(R.id.lvCartItems)

        // 2. Setup ListView
        // We use the adapter from Dashboard.
        adapter = Dashboard.ProductAdapter(this, CartManager.cartList)
        listView.adapter = adapter

        updateTotals()

        // 3. Back Button logic
        findViewById<ImageView>(R.id.btnBack)?.setOnClickListener {
            finish()
        }

        // 4. Long click to remove item from cart
        listView.setOnItemLongClickListener { _, _, position, _ ->
            val removedItem = CartManager.cartList[position]
            CartManager.cartList.removeAt(position)
            adapter.notifyDataSetChanged()
            updateTotals()
            Toast.makeText(this, "Removed ${removedItem.name}", Toast.LENGTH_SHORT).show()
            true
        }

        // 5. Checkout Button logic
        findViewById<Button>(R.id.btnCheckout).setOnClickListener {
            if (CartManager.cartList.isEmpty()) {
                Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show()
            } else {
                // We convert the MutableList to an ArrayList to satisfy Parcelable requirements
                val intent = Intent(this, CheckoutActivity::class.java).apply {
                    putParcelableArrayListExtra("SELECTED_PRODUCTS", ArrayList(CartManager.cartList))
                }
                startActivity(intent)
            }
        }
    }

    // Helper function to calculate the total price
    private fun updateTotals() {
        val totalAmount = CartManager.cartList.sumOf { it.price }
        val itemCount = CartManager.cartList.size

        // Formats to 2 decimal places: ₱ 1,234.50
        tvTotal.text = "₱ ${"%.2f".format(totalAmount)}"
        tvSubtotal.text = "$itemCount Items"
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
        updateTotals()
    }
}