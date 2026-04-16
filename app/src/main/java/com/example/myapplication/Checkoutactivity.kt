package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CheckoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        // ✅ RECEIVE data passed via Intent from CartActivity or ProductDetailActivity
        val productName  = intent.getStringExtra("checkout_product_name")  ?: "Unknown Product"
        val productPrice = intent.getDoubleExtra("checkout_product_price", 0.0)
        val qty          = intent.getIntExtra("checkout_qty", 1)
        val seller       = intent.getStringExtra("checkout_seller")        ?: "Lazada Seller"
        // If total was pre-calculated by CartActivity use it; otherwise compute here
        val total        = intent.getDoubleExtra("checkout_total", productPrice * qty)

        // Bind order summary
        findViewById<TextView>(R.id.tvOrderProductName).text  = productName
        findViewById<TextView>(R.id.tvOrderSeller).text       = "Seller: $seller"
        findViewById<TextView>(R.id.tvOrderQty).text          = "Qty: $qty"
        findViewById<TextView>(R.id.tvOrderUnitPrice).text    = "Unit: ₱ ${"%.2f".format(productPrice)}"
        findViewById<TextView>(R.id.tvOrderTotal).text        = "Total: ₱ ${"%.2f".format(total)}"

        // Back button
        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // Place Order button → navigate to Order Confirmation screen
        findViewById<Button>(R.id.btnPlaceOrder).setOnClickListener {
            val orderId = "ORD-${System.currentTimeMillis()}"

            Toast.makeText(this, "Order $orderId placed! 🎉", Toast.LENGTH_LONG).show()

            // Pass order confirmation data to HomeActivity (clear back stack)
            val intent = Intent(this, HomeActivity::class.java).apply {
                putExtra("order_id",           orderId)
                putExtra("order_product_name", productName)
                putExtra("order_total",        total)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            startActivity(intent)
            finish()
        }
    }
}