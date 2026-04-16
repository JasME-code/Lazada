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
        setContentView(R.layout.activity_product_detail)

        // ✅ RECEIVE data passed via Intent from HomeActivity (Dashboard)
        val productName    = intent.getStringExtra("product_name")    ?: "Unknown Product"
        val productPrice   = intent.getDoubleExtra("product_price", 0.0)
        val productRating  = intent.getFloatExtra("product_rating", 0f)
        val productSeller  = intent.getStringExtra("product_seller")  ?: "Lazada Seller"
        val productImageId = intent.getIntExtra("product_image_res", R.drawable.img)

        // Bind data to views
        findViewById<TextView>(R.id.tvProductName).text    = productName
        findViewById<TextView>(R.id.tvProductPrice).text   = "₱ ${"%.2f".format(productPrice)}"
        findViewById<TextView>(R.id.tvProductRating).text  = "⭐ $productRating / 5.0"
        findViewById<TextView>(R.id.tvProductSeller).text  = "Sold by: $productSeller"
        findViewById<ImageView>(R.id.ivProductImage).setImageResource(productImageId)

        // Back button
        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // Add to Cart button → passes product data to CartActivity
        findViewById<Button>(R.id.btnAddToCart).setOnClickListener {
            val intent = Intent(this, CartActivity::class.java).apply {
                putExtra("cart_product_name",  productName)
                putExtra("cart_product_price", productPrice)
                putExtra("cart_product_qty",   1)
            }
            startActivity(intent)
        }

        // Buy Now button → skips cart, goes straight to CheckoutActivity
        findViewById<Button>(R.id.btnBuyNow).setOnClickListener {
            val intent = Intent(this, CheckoutActivity::class.java).apply {
                putExtra("checkout_product_name",  productName)
                putExtra("checkout_product_price", productPrice)
                putExtra("checkout_seller",        productSeller)
                putExtra("checkout_qty",           1)
            }
            startActivity(intent)
        }
    }
}