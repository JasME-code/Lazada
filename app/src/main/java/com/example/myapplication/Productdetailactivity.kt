package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProductDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activityproductdetail)

        // 1. RECEIVE: Data from Dashboard (using the Product object we sent)
        // We use getParcelableExtra because we sent the whole object
        val product = intent.getParcelableExtra<Product>("PRODUCT_DATA")

        if (product != null) {
            // DISPLAY: Map data to your XML IDs
            findViewById<TextView>(R.id.tvProductName).text   = product.name
            findViewById<TextView>(R.id.tvProductPrice).text  = "₱ ${"%.2f".format(product.price)}"
            findViewById<TextView>(R.id.tvProductRating).text = "⭐ ${product.rating} / 5.0"
            findViewById<TextView>(R.id.tvProductSeller).text = "Sold by: ${product.seller}"
            findViewById<ImageView>(R.id.ivProductImage).setImageResource(product.imageRes)

            // 2. BACK BUTTON
            findViewById<ImageView>(R.id.btnBack).setOnClickListener { finish() }

            // 3. ADD TO GLOBAL CART
            findViewById<Button>(R.id.btnAddToCart).setOnClickListener {
                CartManager.cartList.add(product)

                Toast.makeText(this, "${product.name} added to cart! 🛒", Toast.LENGTH_SHORT).show()

                // Optional: Close this screen so user can pick more items on Dashboard
                finish()
            }

            // 4. BUY NOW (Straight to Checkout with just this one item)
            findViewById<Button>(R.id.btnBuyNow).setOnClickListener {
                val intent = Intent(this, CheckoutActivity::class.java).apply {
                    // We put this single item into a list because Checkout expects a list
                    val singleItemList = arrayListOf(product)
                    putParcelableArrayListExtra("SELECTED_PRODUCTS", singleItemList)
                }
                startActivity(intent)
            }
        }
    }
}