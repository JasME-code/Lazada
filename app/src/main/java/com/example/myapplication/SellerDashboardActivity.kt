package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class SellerDashboardActivity : AppCompatActivity() {

    // Move views to class level so they can be accessed by all functions
    private lateinit var tvRevenue: TextView
    private lateinit var tvCount: TextView
    private lateinit var lvOrders: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_dashboard)

        // 1. INITIALIZE UI
        tvRevenue = findViewById(R.id.tvTotalRevenue)
        tvCount = findViewById(R.id.tvTotalItemsSold)
        lvOrders = findViewById(R.id.lvOrdersToFulfill)
        val btnAdd = findViewById<Button>(R.id.btnAddProduct)
        val btnEdit = findViewById<Button>(R.id.btnViewListings)
        val btnLogout = findViewById<ImageView>(R.id.btnLogout)

        // 2. REFRESH UI DATA
        refreshUI()

        // 3. ORDER FULFILLMENT LOGIC
        lvOrders.setOnItemClickListener { _, _, position, _ ->
            val order = OrderManager.orderHistory[position]

            when (order.status) {
                "Pending" -> {
                    order.status = "Shipped"
                    Toast.makeText(this, "Order #$position Shipped! 🚚", Toast.LENGTH_SHORT).show()
                }
                "Shipped" -> {
                    order.status = "Delivered"
                    // Update Seller Stats only upon delivery
                    SellerManager.totalSales += order.totalPrice
                    SellerManager.itemsSold += 1
                    Toast.makeText(this, "Order Delivered! Revenue Updated 💰", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this, "Order already finalized.", Toast.LENGTH_SHORT).show()
                }
            }
            refreshUI() // Update the UI immediately after status change
        }

        // 4. NAVIGATION
        btnAdd.setOnClickListener {
            // This Activity must use Dashboard.productList.add(newProduct) to keep all items
            startActivity(Intent(this, AddProductActivity::class.java))
        }

        btnEdit.setOnClickListener {
            // startActivity(Intent(this, EditListingsActivity::class.java))
            Toast.makeText(this, "Opening listings...", Toast.LENGTH_SHORT).show()
        }

        btnLogout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Logout") { _, _ ->
                    // 1. Point to your Login Activity
                    val intent = Intent(this, MainActivity::class.java)

                    // 2. CLEAR THE STACK
                    // This is crucial for security so users can't click 'Back' to return to the dashboard
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)

                    // 3. Close the dashboard
                    finish()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    // Centralized function to update the screen
    private fun refreshUI() {
        tvRevenue.text = "₱ ${"%.2f".format(SellerManager.totalSales)}"
        tvCount.text = "Items Sold: ${SellerManager.itemsSold}"

        // Create a list of strings to show in the ListView
        val orderStrings = OrderManager.orderHistory.map {
            "Order ID: ${it.orderId}\nStatus: ${it.status} • Total: ₱${it.totalPrice}"
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, orderStrings)
        lvOrders.adapter = adapter
    }

    // Refresh data when returning from AddProduct or EditListings
    override fun onResume() {
        super.onResume()
        refreshUI()
    }
}