package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class ProductAdapter(context: Context, products: List<Product>) :
    ArrayAdapter<Product>(context, 0, products) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_product_list, parent, false)

        val product = getItem(position)!!

        view.findViewById<TextView>(R.id.tvProductName).text = product.name
        view.findViewById<TextView>(R.id.tvProductPrice).text = "₱ ${"%.0f".format(product.price)}"
        view.findViewById<TextView>(R.id.tvProductRating).text = "⭐ ${product.rating}"
        view.findViewById<TextView>(R.id.tvProductSeller).text = product.seller
        view.findViewById<ImageView>(R.id.ivProductImage).setImageResource(product.imageRes)

        // Click → ProductDetailActivity
        view.setOnClickListener {
            Intent(context, ProductDetailActivity::class.java).apply {
                putExtra("product_name", product.name)
                putExtra("product_price", product.price)
                putExtra("product_rating", product.rating)
                putExtra("product_seller", product.seller)
                putExtra("product_image_res", product.imageRes)
                context.startActivity(this)
            }
        }
        return view
    }
}