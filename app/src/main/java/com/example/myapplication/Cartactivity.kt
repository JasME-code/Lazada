package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

// FIX 1: setContentView uses R.layout.activitycart (actual file is activitycart.xml)
// FIX 2: updateTotals() moved to class scope — it was a local function inside onCreate
//         which caused a compile error (local functions can't reference outer vars cleanly in Kotlin like this)
// FIX 3: productPrice and quantity are now class-level properties so updateTotals() can access them
// FIX 4: Checkout Intent now also passes "checkout_seller" received from ProductDetailActivity

class CartActivity : AppCompatActivity() {

    private var quantity     = 1
    private var productPrice = 0.0
    private lateinit var tvQty:      TextView
    private lateinit var tvSubtotal: TextView
    private lateinit var tvTotal:    TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activitycart)  // FIX: was R.layout.activity_cart

        // Receive Intent data from ProductDetailActivity
        val productName   = intent.getStringExtra("cart_product_name")   ?: "Unknown Product"
        val productSeller = intent.getStringExtra("cart_product_seller") ?: "Lazada Seller"
        productPrice      = intent.getDoubleExtra("cart_product_price",  0.0)
        quantity          = intent.getIntExtra("cart_product_qty",        1)

        val tvProductName = findViewById<TextView>(R.id.tvCartProductName)
        val tvUnitPrice   = findViewById<TextView>(R.id.tvCartUnitPrice)
        tvQty             = findViewById(R.id.tvCartQty)
        tvSubtotal        = findViewById(R.id.tvCartSubtotal)
        tvTotal           = findViewById(R.id.tvCartTotal)

        tvProductName.text = productName
        tvUnitPrice.text   = "₱ ${"%.2f".format(productPrice)}"
        updateTotals()

        // Minus button
        findViewById<ImageView>(R.id.btnQtyMinus).setOnClickListener {
            if (quantity > 1) {
                quantity--
                updateTotals()
            } else {
                Toast.makeText(this, "Minimum quantity is 1", Toast.LENGTH_SHORT).show()
            }
        }

        // Plus button
        findViewById<ImageView>(R.id.btnQtyPlus).setOnClickListener {
            quantity++
            updateTotals()
        }

        // Back button
        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // Proceed to Checkout — passes all order data
        findViewById<Button>(R.id.btnCheckout).setOnClickListener {
            val totalAmount = productPrice * quantity
            val intent = Intent(this, CheckoutActivity::class.java).apply {
                putExtra("checkout_product_name",  productName)
                putExtra("checkout_product_price", productPrice)
                putExtra("checkout_seller",        productSeller)  // FIX: seller was missing before
                putExtra("checkout_qty",           quantity)
                putExtra("checkout_total",         totalAmount)
            }
            startActivity(intent)
        }
    }

    // FIX: Moved to class scope so it can access class-level quantity and productPrice
    private fun updateTotals() {
        val subtotal = productPrice * quantity
        tvQty.text      = quantity.toString()
        tvSubtotal.text = "₱ ${"%.2f".format(subtotal)}"
        tvTotal.text    = "₱ ${"%.2f".format(subtotal)}"
    }
}