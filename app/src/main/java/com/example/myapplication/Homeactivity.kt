package com.example.myapplication

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.textfield.TextInputEditText

// FIX 1: setContentView now uses R.layout.dashboard (the actual file is dashboard.xml)
// FIX 2: Timer crash fixed — secs value guarded with toIntOrNull() so it won't crash on empty/non-int text
// FIX 3: Category buttons now launch ProductDetailActivity with real Intent data instead of just Toasts
// FIX 4: btnShopNow (hero banner) now launches ProductDetailActivity with Intent data
// FIX 5: Logout now correctly clears back stack back to SplashActivity

class HomeActivity : AppCompatActivity() {

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
        setContentView(R.layout.dashboard)  // FIX: was R.layout.activity_home (file doesn't exist)

        setupViews()
        setupUserData()
        setupClickListeners()
        startTimerAnimation()
    }

    private fun setupViews() {
        appBarLayout      = findViewById(R.id.appBarLayout)
        collapsingToolbar = findViewById(R.id.collapsingToolbar)
        promoStrip        = findViewById(R.id.promoStrip)
        headerFull        = findViewById(R.id.headerFull)
        compactToolbar    = findViewById(R.id.compactToolbar)

        setupSearch(findViewById(R.id.etSearchFull),    findViewById(R.id.btnSearchFull))
        setupSearch(findViewById(R.id.etSearchCompact), findViewById(R.id.btnSearchCompact))
    }

    private fun setupUserData() {
        // Receive user data passed from Login or Register
        val userEmail = intent.getStringExtra("user_email") ?: "User"
        val userName  = intent.getStringExtra("user_name")  ?: userEmail
        Toast.makeText(this, "Welcome, $userName! 🎉", Toast.LENGTH_SHORT).show()
    }

    private fun setupClickListeners() {
        // FIX: Hero banner "Shop Now" now opens ProductDetailActivity with data
        findViewById<Button>(R.id.btnShopNow).setOnClickListener {
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

    // FIX: Category buttons now navigate to ProductDetailActivity with Intent extras
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

    // Helper: builds the Intent and launches ProductDetailActivity
    private fun openProduct(
        name: String,
        price: Double,
        rating: Float,
        seller: String,
        imageRes: Int = R.drawable.img
    ) {
        val intent = Intent(this, ProductDetailActivity::class.java).apply {
            putExtra("product_name",      name)
            putExtra("product_price",     price)
            putExtra("product_rating",    rating)
            putExtra("product_seller",    seller)
            putExtra("product_image_res", imageRes)
        }
        startActivity(intent)
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

    // FIX: Timer now uses its own tracked variables instead of parsing TextView text
    //      (parsing caused NumberFormatException crash when text was empty or non-numeric)
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
        animateTimerDigit(minsView,  "%02d".format(timerMinutes))
        animateTimerDigit(secsView,  "%02d".format(timerSeconds))
    }

    private fun animateTimerDigit(view: TextView, newValue: String) {
        val animator = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.8f, 1f)
        animator.duration = 200
        animator.start()
        view.text = newValue
    }

    // FIX: Logout clears entire back stack so user can't press Back to return to dashboard
    fun logout(view: View) {
        val intent = Intent(this, SplashActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }
}