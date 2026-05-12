package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CheckoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activitycheckout)

        // 1. DATA RETRIEVAL: Get items sent from Cart
        val selectedProducts = intent.getParcelableArrayListExtra<Product>("SELECTED_PRODUCTS") ?: arrayListOf()

        // 2. SETUP VIEWS
        val lvCheckoutItems = findViewById<ListView>(R.id.lvCheckoutItems)
        val tvTotalAmount   = findViewById<TextView>(R.id.tvOrderTotal)
        val tvItemQty       = findViewById<TextView>(R.id.tvOrderQty)
        val btnPlaceOrder   = findViewById<Button>(R.id.btnPlaceOrder)
        val btnBack         = findViewById<ImageView>(R.id.btnBack)
        val etAddress = findViewById<EditText>(R.id.etAddress)
        val spinnerPayment = findViewById<Spinner>(R.id.spinnerPayment)

        // 3. SETUP SPINNER (Payment Methods)
        val paymentOptions =
            arrayOf("Cash on Delivery (COD)", "GCash", "Lazada Wallet", "Credit Card")
        val paymentAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, paymentOptions)
        spinnerPayment.adapter = paymentAdapter

        // 4. ATTACH ADAPTER (Dashboard.ProductAdapter reused)
        val adapter = Dashboard.ProductAdapter(this, selectedProducts)
        lvCheckoutItems.adapter = adapter

        // 5. CALCULATE TOTALS
        val totalAmount = selectedProducts.sumOf { it.price }
        val itemCount   = selectedProducts.size

        tvTotalAmount.text = "₱ ${"%.2f".format(totalAmount)}"
        tvItemQty.text     = "Total ($itemCount items)"

        // 6. BACK BUTTON
        btnBack.setOnClickListener { finish() }

        // 7. PLACE ORDER BUTTON
        btnPlaceOrder.setOnClickListener {
            val address = etAddress.text.toString().trim()
            val paymentMethod = spinnerPayment.selectedItem.toString()

            if (selectedProducts.isEmpty()) {
                Toast.makeText(this, "No items to order!", Toast.LENGTH_SHORT).show()
            } else if (address.isEmpty()) {
                Toast.makeText(this, "Please enter your shipping address!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                // --- CREATE ORDER RECORD FOR TRACKING ---
                val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                val currentDate = sdf.format(Date())

                val newOrder = Order(
                    orderId = "ORD-${(10000..99999).random()}",
                    items = ArrayList(selectedProducts),
                    totalPrice = totalAmount,
                    address = address,
                    paymentMethod = paymentMethod,
                    date = currentDate,
                    status = "Pending" // Start with Pending status
                )

                // Save to Global List in Product.kt
                OrderManager.orderHistory.add(newOrder)

                Toast.makeText(this, "Order Placed Successfully! 🎉", Toast.LENGTH_LONG).show()

                // CLEAR THE GLOBAL CART: Now empty for next time
                CartManager.cartList.clear()

                // RETURN TO DASHBOARD
                val intent = Intent(this, Dashboard::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }
    }
}