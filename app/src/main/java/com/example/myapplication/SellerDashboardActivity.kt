package com.example.myapplication

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class SellerDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_dashboard)

        // 1. Initial Summary Update
        updateSalesSummary()

        // 2. Setup Orders List
        val lvOrders = findViewById<ListView>(R.id.lvOrdersToFulfill)

        // Simple Adapter using Order History from Product.kt
        val orderStrings = OrderManager.orderHistory.map {
            "Order: ${it.orderId}\nStatus: ${it.status}\nTotal: ₱${"%.2f".format(it.totalPrice)}"
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, orderStrings)
        lvOrders.adapter = adapter

        // 3. Fulfill Logic: Pending -> Shipped -> Delivered
        lvOrders.setOnItemClickListener { _, _, position, _ ->
            val order = OrderManager.orderHistory[position]

            when (order.status) {
                "Pending" -> {
                    order.status = "Shipped"
                    Toast.makeText(this, "Marked as Shipped!", Toast.LENGTH_SHORT).show()
                }

                "Shipped" -> {
                    order.status = "Delivered"
                    // Add to total sales summary only when delivered
                    SellerManager.totalSales += order.totalPrice
                    SellerManager.itemsSold += order.items.size
                    Toast.makeText(this, "Marked as Delivered! Sales Updated.", Toast.LENGTH_SHORT)
                        .show()
                }

                else -> Toast.makeText(this, "Order is already completed.", Toast.LENGTH_SHORT)
                    .show()
            }

            // Refresh screen to show new status and updated sales
            updateSalesSummary()
            recreate()
        }

        findViewById<Button>(R.id.btnAddProduct).setOnClickListener {
            Toast.makeText(this, "Add Product feature coming soon", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateSalesSummary() {
        val tvRevenue = findViewById<TextView>(R.id.tvTotalRevenue)
        val tvCount = findViewById<TextView>(R.id.tvTotalItemsSold)

        tvRevenue.text = "₱ ${"%.2f".format(SellerManager.totalSales)}"
        tvCount.text = "Items Sold: ${SellerManager.itemsSold}"
    }
}