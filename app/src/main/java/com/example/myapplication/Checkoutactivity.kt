package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

// FIX 1: setContentView uses R.layout.activitycheckout (actual file is activitycheckout.xml)
// FIX 2: Reads "checkout_seller" extra that was missing from previous version
// FIX 3: Place Order clears the full back stack so user can't press Back into checkout again
// FIX 4: Intent flags set correctly using apply block (previous version set flags inside apply but also outside)

class CheckoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        // Back button
        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // Place Order
        findViewById<Button>(R.id.btnPlaceOrder).setOnClickListener {
            val orderId = "ORD-${System.currentTimeMillis()}"
            Toast.makeText(this, "Order $orderId placed! 🎉", Toast.LENGTH_LONG).show()

            // Return to HomeActivity and clear all activities in between
            // FIX: Flags correctly applied inside apply block
            val intent = Intent(this, HomeActivity::class.java).apply {
                putExtra("order_id",           orderId)
                putExtra("order_product_name", productName)
                putExtra("order_total",        total)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
            finish()
        }
    }
}