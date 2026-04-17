package com.example.myapplication

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.textfield.TextInputEditText

// 🔥 DATA CLASS
data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val rating: Float,
    val seller: String,
    val imageRes: Int = R.drawable.img
)

// 🔥 ARRAY ADAPTER
class ProductAdapter(
    private val context: AppCompatActivity,
    private val products: List<Product>
) : android.widget.ArrayAdapter<Product>(context, 0, products) {

    override fun getView(position: Int, convertView: View?, parent: android.view.ViewGroup): View {
        val view = convertView ?: android.view.LayoutInflater.from(context)
            .inflate(R.layout.item_product_list, parent, false)

        val product = getItem(position)!!

        view.findViewById<TextView>(R.id.tvProductName).text = product.name
        view.findViewById<TextView>(R.id.tvProductPrice).text = "₱ ${"%.0f".format(product.price)}"
        view.findViewById<TextView>(R.id.tvProductRating).text = "⭐ ${product.rating}"
        view.findViewById<TextView>(R.id.tvProductSeller).text = product.seller
        view.findViewById<android.widget.ImageView>(R.id.ivProductImage).setImageResource(product.imageRes)

        // Click → ProductDetailActivity (your existing flow!)
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

class HomeActivity : AppCompatActivity() {

    // 🔥 LISTVIEW PROPERTIES
    private lateinit var listView: ListView
    private lateinit var productAdapter: ProductAdapter

    // 🔥 FLASH SALE DATA
    private val flashSaleProducts = listOf(
        Product(1, "Nike Pegasus 42", 5999.0, 4.9f, "Nike Official", R.drawable.img),
        Product(2, "Razer BlackShark V2", 3999.0, 4.8f, "Razer Store", R.drawable.img),
        Product(3, "LEGO Classic 500pcs", 1799.0, 4.9f, "Toy Kingdom", R.drawable.img),
        Product(4, "Skincare Premium Set", 1299.0, 4.5f, "Beauty Hub", R.drawable.img),
        Product(5, "Kids Learning Tablet", 2499.0, 4.7f, "EduTech", R.drawable.img),
        Product(6, "Samsung Galaxy Buds", 3499.0, 4.6f, "Samsung Store", R.drawable.img),
        Product(7, "iPhone Case Premium", 899.0, 4.8f, "CaseKing", R.drawable.img)
    )

    private lateinit var appBarLayout: AppBarLayout
    private lateinit var collapsingToolbar: CollapsingToolbarLayout
    private lateinit var promoStrip: View
    private lateinit var headerFull: View
    private lateinit var compactToolbar: Toolbar

    private var timerSeconds = 59
    private var timerMinutes = 59
    private var timerHours   = 11

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard)

        // 🔥 LISTVIEW SETUP (FIRST!)
        setupProductListView()

        // Your existing setup
        setupViews()
        setupUserData()
        setupClickListeners()
        startTimerAnimation()
    }

    // 🔥 LISTVIEW SETUP
    private fun setupProductListView() {
        listView = findViewById(R.id.listViewFlashSale)
        productAdapter = ProductAdapter(this, flashSaleProducts)
        listView.adapter = productAdapter
    }

    private fun setupViews() {
        appBarLayout      = findViewById(R.id.appBarLayout)
        collapsingToolbar = findViewById(R.id.collapsingToolbar)
        promoStrip        = findViewById(R.id.promoStrip)
        headerFull        = findViewById(R.id.headerFull)
        compactToolbar    = findViewById(R.id.compactToolbar)

        setupSearch(findViewById(R.id.etSearchFull), findViewById(R.id.btnSearchFull))
        setupSearch(findViewById(R.id.etSearchCompact), findViewById(R.id.btnSearchCompact))
    }

    private fun setupUserData() {
        val userEmail = intent.getStringExtra("user_email") ?: "User"
        val userName  = intent.getStringExtra("user_name")  ?: userEmail
        Toast.makeText(this, "Welcome, $userName! 🎉", Toast.LENGTH_SHORT).show()
    }

    private fun setupClickListeners() {
        findViewById<View>(R.id.btnShopNow).setOnClickListener {
            openProduct("Nike Pegasus 42", 5999.00, 4.9f, "Nike Official Store")
        }

        findViewById<View>(R.id.btnTopUp).setOnClickListener {
            Toast.makeText(this, "Top Up feature coming soon!", Toast.LENGTH_SHORT).show()
        }

        findViewById<View>(R.id.btnLazMall).setOnClickListener {
            Toast.makeText(this, "LazMall — browse top brands!", Toast.LENGTH_SHORT).show()
        }

        findViewById<TextView>(R.id.tvShopAll).setOnClickListener {
            Toast.makeText(this, "Viewing all flash sale items", Toast.LENGTH_SHORT).show()
        }

        setupCategoryButtons()
    }

    private fun setupSearch(searchEditText: TextInputEditText, searchButton: View) {
        searchButton.setOnClickListener {
            val query = searchEditText.text.toString().trim()
            if (query.isNotEmpty()) {
                Toast.makeText(this, "Searching for: $query 🔍", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Enter a search term", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupCategoryButtons() {
        findViewById<View>(R.id.btnGamingProducts)?.setOnClickListener {
            openProduct("Razer BlackShark V2 Headset", 3999.00, 4.8f, "Razer Official Store")
        }

        findViewById<View>(R.id.btnAdultProducts)?.setOnClickListener {
            openProduct("Premium Skincare Set", 1299.00, 4.5f, "Beauty Hub PH")
        }

        findViewById<View>(R.id.btnKidsProducts)?.setOnClickListener {
            openProduct("Kids Learning Tablet", 2499.00, 4.7f, "EduTech Store")
        }

        findViewById<View>(R.id.btnToysProducts)?.setOnClickListener {
            openProduct("LEGO Classic Set 500pcs", 1799.00, 4.9f, "Toy Kingdom PH")
        }
    }

    private fun openProduct(name: String, price: Double, rating: Float, seller: String, imageRes: Int = R.drawable.img) {
        Intent(this, ProductDetailActivity::class.java).apply {
            putExtra("product_name", name)
            putExtra("product_price", price)
            putExtra("product_rating", rating)
            putExtra("product_seller", seller)
            putExtra("product_image_res", imageRes)
            startActivity(this)
        }
    }

    private fun startTimerAnimation() {
        val handler = Handler(Looper.getMainLooper())
        val timerRunnable = object : Runnable {
            override fun run() {
                updateTimer()
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(timerRunnable)
    }

    private fun updateTimer() {
        timerSeconds--
        if (timerSeconds < 0) {
            timerSeconds = 59
            timerMinutes--
            if (timerMinutes < 0) {
                timerMinutes = 59
                timerHours--
                if (timerHours < 0) {
                    timerHours = 0; timerMinutes = 0; timerSeconds = 0
                }
            }
        }

        val hoursView = findViewById<TextView>(R.id.tvTimerHours)
        val minsView  = findViewById<TextView>(R.id.tvTimerMins)
        val secsView  = findViewById<TextView>(R.id.tvTimerSecs)

        animateTimerDigit(hoursView, "%02d".format(timerHours))
        animateTimerDigit(minsView, "%02d".format(timerMinutes))
        animateTimerDigit(secsView, "%02d".format(timerSeconds))
    }

    private fun animateTimerDigit(view: TextView, newValue: String) {
        val animator = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.8f, 1f)
        animator.duration = 200
        animator.start()
        view.text = newValue
    }

    fun logout(view: View) {
        Intent(this, SplashActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(this)
        }
    }
}