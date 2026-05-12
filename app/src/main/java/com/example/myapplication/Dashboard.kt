package com.example.myapplication

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

    // COMPANION OBJECT: This makes the list "Global"
    // Now SellerDashboard can do: Dashboard.productList.add(item)
    companion object {
        val productList = mutableListOf<Product>()
    }

    private var displayList = mutableListOf<Product>()
    private lateinit var productAdapter: ProductAdapter
    private var userAge: Int = 0

    private var timerSeconds = 59
    private var timerMinutes = 59
    private var timerHours   = 11

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard)

        // Initialize with default items ONLY if the list is empty
        if (productList.isEmpty()) {
            loadDefaultProducts()
        }

        val user = intent.getParcelableExtra<User>("USER_DATA")
        userAge = user?.age ?: 0

        setupViews(user)
        setupProductList()
        setupFilters()
        setupClickListeners(user?.accountType ?: "Customer")
        startTimerAnimation()
        simulateOrderUpdateNotification()
    }

    // Refresh the list whenever we return from the Seller screen
    override fun onResume() {
        super.onResume()
        filterAndRefreshList()
    }

    private fun loadDefaultProducts() {
        productList.add(
            Product(
                1,
                "Nike Pegasus 42",
                5999.0,
                4.9f,
                "Nike Official",
                R.drawable.img,
                "Shoes",
                15,
                "Responsive foam.",
                listOf("Comfy"),
                false
            )
        )
        productList.add(
            Product(
                2,
                "Restricted Item (18+)",
                9999.0,
                4.8f,
                "Premium Store",
                R.drawable.img,
                "Electronics",
                5,
                "Restricted access.",
                listOf("Quality"),
                true
            )
        )
        productList.add(
            Product(
                3,
                "Razer BlackShark V2",
                3999.0,
                4.7f,
                "Razer Store",
                R.drawable.img,
                "Electronics",
                0,
                "Spatial Audio.",
                listOf("Best mic"),
                false
            )
        )
    }

    private fun setupViews(user: User?) {
        val welcome = if (user != null) "Welcome, ${user.name}!" else "Welcome!"
        Toast.makeText(this, welcome, Toast.LENGTH_SHORT).show()

        setupSearch(findViewById(R.id.etSearchFull), findViewById(R.id.btnSearchFull))
    }

    private fun setupProductList() {
        val lvFlash = findViewById<ListView>(R.id.listViewFlashSale)

        productAdapter = ProductAdapter(this, displayList)
        lvFlash.adapter = productAdapter

        filterAndRefreshList()

        lvFlash.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val selected = parent.getItemAtPosition(position) as Product
            val intent = Intent(this, ProductDetailActivity::class.java).apply {
                putExtra("PRODUCT_DATA", selected)
                putExtra("USER_AGE", userAge)
            }
            startActivity(intent)
        }
    }

    private fun filterAndRefreshList() {
        val filtered = if (userAge < 18) {
            productList.filter { !it.isRestricted }
        } else {
            productList
        }

        displayList.clear()
        displayList.addAll(filtered)
        productAdapter.notifyDataSetChanged()
    }

    private fun setupFilters() {
        findViewById<Button>(R.id.btnSortPrice)?.setOnClickListener {
            displayList.sortBy { it.price }
            productAdapter.notifyDataSetChanged()
        }
    }

    private fun setupClickListeners(role: String) {
        // Redirect to Seller Dashboard if they are a seller
        findViewById<View>(R.id.btnLazMall)?.setOnClickListener {
            if (role == "Seller") {
                startActivity(Intent(this, SellerDashboardActivity::class.java))
            } else {
                Toast.makeText(this, "LazMall is for Sellers only", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<View>(R.id.btnCartIcon)?.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
    }

    private fun setupSearch(et: TextInputEditText?, btn: View?) {
        btn?.setOnClickListener {
            val query = et?.text.toString().lowercase()
            val searched = productList.filter {
                it.name.lowercase().contains(query) && (userAge >= 18 || !it.isRestricted)
            }
            displayList.clear()
            displayList.addAll(searched)
            productAdapter.notifyDataSetChanged()
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

    private fun simulateOrderUpdateNotification() {
        Handler(Looper.getMainLooper()).postDelayed({
            Toast.makeText(
                this,
                "🔔 STATUS UPDATE: Order #LAZ-990 has been SHIPPED!",
                Toast.LENGTH_LONG
            ).show()
        }, 8000)
    }

    class ProductAdapter(context: Context, private val products: List<Product>) : BaseAdapter() {
        private val inflater = LayoutInflater.from(context)
        override fun getCount() = products.size
        override fun getItem(p: Int) = products[p]
        override fun getItemId(p: Int) = p.toLong()
        override fun getView(p: Int, v: View?, parent: ViewGroup?): View {
            val view = v ?: inflater.inflate(android.R.layout.simple_list_item_2, parent, false)
            val item = getItem(p)
            view.findViewById<TextView>(android.R.id.text1).text = item.name
            view.findViewById<TextView>(android.R.id.text2).text = "₱${item.price} • ${item.seller}"
            return view
        }
    }
}