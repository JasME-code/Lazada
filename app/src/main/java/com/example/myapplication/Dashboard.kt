package com.example.myapplication

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class Dashboard : AppCompatActivity() {

    // Data for the 3 different lists
    private val flashSaleProducts = listOf(
        Product(1, "Nike Pegasus 42", 5999.0, 4.9f, "Nike Official", R.drawable.img),
        Product(2, "Razer BlackShark V2", 3999.0, 4.8f, "Razer Store", R.drawable.img),
        Product(3, "LEGO Classic 500pcs", 1799.0, 4.9f, "Toy Kingdom", R.drawable.img),
        Product(4, "Samsung Galaxy Buds", 3499.0, 4.6f, "Samsung Store", R.drawable.img)
    )

    private val topBrands = listOf(
        Product(101, "Samsung Galaxy S24", 58999.0, 4.9f, "Samsung Official", R.drawable.img),
        Product(102, "Sony WH-1000XM5", 19999.0, 4.8f, "Sony Store", R.drawable.img)
    )

    private val recommendedItems = listOf(
        Product(201, "Cotton T-Shirt", 499.0, 4.5f, "Fashion PH", R.drawable.img),
        Product(202, "Minimalist Watch", 2499.0, 4.7f, "TimeKeeper", R.drawable.img)
    )

    private var timerSeconds = 59
    private var timerMinutes = 59
    private var timerHours   = 11

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard)

        setupViews()
        setupThreeLists()
        setupClickListeners()
        startTimerAnimation()
    }

    private fun setupViews() {
        val userEmail = intent.getStringExtra("user_email") ?: "User"
        Toast.makeText(this, "Welcome, $userEmail! 🎉", Toast.LENGTH_SHORT).show()

        setupSearch(findViewById(R.id.etSearchFull), findViewById(R.id.btnSearchFull))
        setupSearch(findViewById(R.id.etSearchCompact), findViewById(R.id.btnSearchCompact))
    }

    private fun setupThreeLists() {
        val lvFlash = findViewById<ListView>(R.id.listViewFlashSale)
        val lvBrands = findViewById<ListView>(R.id.listViewTopBrands)
        val lvRec = findViewById<ListView>(R.id.listViewRecommended)

        lvFlash.adapter = ProductAdapter(this, flashSaleProducts)
        lvBrands.adapter = ProductAdapter(this, topBrands)
        lvRec.adapter = ProductAdapter(this, recommendedItems)

        // CLICK LISTENER: Opens Detail Screen
        val clickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val selectedProduct = parent.getItemAtPosition(position) as Product
            val intent = Intent(this, ProductDetailActivity::class.java).apply {
                putExtra("PRODUCT_DATA", selectedProduct)
            }
            startActivity(intent)
        }

        lvFlash.onItemClickListener = clickListener
        lvBrands.onItemClickListener = clickListener
        lvRec.onItemClickListener = clickListener
    }

    private fun setupClickListeners() {
        // FIXED: Now goes to CartActivity first as the "Barrier"
        findViewById<View>(R.id.btnCartIcon).setOnClickListener {
            if (CartManager.cartList.isEmpty()) {
                Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, CartActivity::class.java)
                startActivity(intent)
            }
        }

        // Quick action buttons
        findViewById<View>(R.id.btnShopNow)?.setOnClickListener { Toast.makeText(this, "Featured Promo", Toast.LENGTH_SHORT).show() }
        findViewById<View>(R.id.btnTopUp)?.setOnClickListener { Toast.makeText(this, "Top Up Service", Toast.LENGTH_SHORT).show() }
        findViewById<View>(R.id.btnLazMall)?.setOnClickListener { Toast.makeText(this, "LazMall Official", Toast.LENGTH_SHORT).show() }
    }

    private fun setupSearch(searchEditText: TextInputEditText?, searchButton: View?) {
        searchButton?.setOnClickListener {
            val query = searchEditText?.text.toString().trim()
            if (query.isNotEmpty()) Toast.makeText(this, "Searching: $query", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startTimerAnimation() {
        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                updateTimer()
                handler.postDelayed(this, 1000)
            }
        })
    }

    private fun updateTimer() {
        if (--timerSeconds < 0) {
            timerSeconds = 59
            if (--timerMinutes < 0) {
                timerMinutes = 59
                if (--timerHours < 0) timerHours = 0
            }
        }
        findViewById<TextView>(R.id.tvTimerHours)?.text = "%02d".format(timerHours)
        findViewById<TextView>(R.id.tvTimerMins)?.text = "%02d".format(timerMinutes)
        findViewById<TextView>(R.id.tvTimerSecs)?.text = "%02d".format(timerSeconds)
    }

    // --- ADAPTER ---
    class ProductAdapter(context: Context, private val products: List<Product>) : BaseAdapter() {
        private val inflater = LayoutInflater.from(context)
        override fun getCount() = products.size
        override fun getItem(position: Int) = products[position]
        override fun getItemId(position: Int) = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: inflater.inflate(android.R.layout.simple_list_item_2, parent, false)
            val product = getItem(position)
            view.findViewById<TextView>(android.R.id.text1).text = product.name
            view.findViewById<TextView>(android.R.id.text2).text = "₱${product.price} - ${product.seller}"
            return view
        }
    }
}