package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
<<<<<<< HEAD
import android.widget.ListView
=======
>>>>>>> origin/master
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

<<<<<<< HEAD
=======
// FIX 1: setContentView uses R.layout.activitycheckout (actual file is activitycheckout.xml)
// FIX 2: Reads "checkout_seller" extra that was missing from previous version
// FIX 3: Place Order clears the full back stack so user can't press Back into checkout again
// FIX 4: Intent flags set correctly using apply block (previous version set flags inside apply but also outside)

>>>>>>> origin/master
class CheckoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
<<<<<<< HEAD
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

=======
        setContentView(R.layout.activitycheckout)  // FIX: was R.layout.activity_checkout
        // Receive Intent data from CartActivity or ProductDetailActivity
        val productName  = intent.getStringExtra("checkout_product_name")  ?: "Unknown Product"
        val productPrice = intent.getDoubleExtra("checkout_product_price", 0.0)
        val qty          = intent.getIntExtra("checkout_qty",              1)
        val seller       = intent.getStringExtra("checkout_seller")        ?: "Lazada Seller"
        val total        = intent.getDoubleExtra("checkout_total",         productPrice * qty)

        // Bind to views
        findViewById<TextView>(R.id.tvOrderProductName).text = productName
        findViewById<TextView>(R.id.tvOrderSeller).text      = "Seller: $seller"
        findViewById<TextView>(R.id.tvOrderQty).text         = "Qty: $qty"
        findViewById<TextView>(R.id.tvOrderUnitPrice).text   = "Unit: ₱ ${"%.2f".format(productPrice)}"
        findViewById<TextView>(R.id.tvOrderTotal).text       = "Total: ₱ ${"%.2f".format(total)}"
>>>>>>> origin/master

        // Back button
        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // Place Order
        findViewById<Button>(R.id.btnPlaceOrder).setOnClickListener {
<<<<<<< HEAD
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
=======
            val orderId = "ORD-${System.currentTimeMillis()}"
            Toast.makeText(this, "Order $orderId placed! 🎉", Toast.LENGTH_LONG).show()

            // Return to HomeActivity and clear all activities in between
            // FIX: Flags correctly applied inside apply block
            val intent = Intent(this, HomeActivity::class.java).apply {
                putExtra("order_id",           orderId)
                putExtra("order_product_name", productName)
                putExtra("order_total",        total)
>>>>>>> origin/master
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
            finish()
        }
    }
}