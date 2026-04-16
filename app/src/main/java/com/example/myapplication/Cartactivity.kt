package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CartActivity : AppCompatActivity() {

    private var quantity = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // ✅ RECEIVE data passed via Intent from ProductDetailActivity
        val productName  = intent.getStringExtra("cart_product_name")  ?: "Unknown Product"
        val productPrice = intent.getDoubleExtra("cart_product_price", 0.0)
        quantity         = intent.getIntExtra("cart_product_qty", 1)

        val tvProductName  = findViewById<TextView>(R.id.tvCartProductName)
        val tvUnitPrice    = findViewById<TextView>(R.id.tvCartUnitPrice)
        val tvQty          = findViewById<TextView>(R.id.tvCartQty)
        val tvSubtotal     = findViewById<TextView>(R.id.tvCartSubtotal)
        val tvTotal        = findViewById<TextView>(R.id.tvCartTotal)

        tvProductName.text = productName
        tvUnitPrice.text   = "₱ ${"%.2f".format(productPrice)}"

        fun updateTotals() {
            tvQty.text      = quantity.toString()
            val subtotal    = productPrice * quantity
            tvSubtotal.text = "₱ ${"%.2f".format(subtotal)}"
            tvTotal.text    = "₱ ${"%.2f".format(subtotal)}"   // add shipping logic here
        }
        updateTotals()

        // Qty controls
        findViewById<ImageView>(R.id.btnQtyMinus).setOnClickListener {
            if (quantity > 1) {
                quantity--
                updateTotals()
            } else {
                Toast.makeText(this, "Minimum quantity is 1", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<ImageView>(R.id.btnQtyPlus).setOnClickListener {
            quantity++
            updateTotals()
        }

        // Back button
        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // Proceed to Checkout → pass consolidated cart data
        findViewById<Button>(R.id.btnCheckout).setOnClickListener {
            val totalAmount = productPrice * quantity
            val intent = Intent(this, CheckoutActivity::class.java).apply {
                putExtra("checkout_product_name",  productName)
                putExtra("checkout_product_price", productPrice)
                putExtra("checkout_qty",           quantity)
                putExtra("checkout_total",         totalAmount)
            }
            startActivity(intent)
        }
    }
}