package com.example.myapplication

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.widget.NestedScrollView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.textfield.TextInputEditText

class HomeActivity : AppCompatActivity() {

    private lateinit var appBarLayout: AppBarLayout
    private lateinit var collapsingToolbar: CollapsingToolbarLayout
    private lateinit var promoStrip: View
    private lateinit var headerFull: View
    private lateinit var compactToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home) // Your advanced dashboard XML

        setupViews()
        setupUserData()
        setupClickListeners()
        startTimerAnimation()
    }

    private fun setupViews() {
        appBarLayout = findViewById(R.id.appBarLayout)
        collapsingToolbar = findViewById(R.id.collapsingToolbar)
        promoStrip = findViewById(R.id.promoStrip)
        headerFull = findViewById(R.id.headerFull)
        compactToolbar = findViewById(R.id.compactToolbar)

        // Search functionality
        setupSearch(findViewById(R.id.etSearchFull), findViewById(R.id.btnSearchFull))
        setupSearch(findViewById(R.id.etSearchCompact), findViewById(R.id.btnSearchCompact))
    }

    private fun setupUserData() {
        val userEmail = intent.getStringExtra("user_email") ?: "User"
        // You can add welcome text here if needed
        Toast.makeText(this, "Welcome to Dashboard, $userEmail! 🎉", Toast.LENGTH_SHORT).show()
    }

    private fun setupClickListeners() {
        // Hero banner
        findViewById<Button>(R.id.btnShopNow).setOnClickListener {
            Toast.makeText(this, "Shop Pegasus 42 Now! 🏃‍♂️", Toast.LENGTH_SHORT).show()
        }

        // Quick links
        findViewById<View>(R.id.btnTopUp).setOnClickListener {
            Toast.makeText(this, "Top Up clicked", Toast.LENGTH_SHORT).show()
        }

        findViewById<View>(R.id.btnLazMall).setOnClickListener {
            Toast.makeText(this, "LazMall clicked", Toast.LENGTH_SHORT).show()
        }

        // Flash sale
        findViewById<TextView>(R.id.tvShopAll).setOnClickListener {
            Toast.makeText(this, "View all flash sale items", Toast.LENGTH_SHORT).show()
        }

        // Category buttons
        setupCategoryButtons()

        // Bottom nav
        setupBottomNavigation()
    }

    private fun setupSearch(searchEditText: TextInputEditText, searchButton: View) {
        searchButton.setOnClickListener {
            val query = searchEditText.text.toString()
            if (query.isNotEmpty()) {
                Toast.makeText(this, "Searching for: $query 🔍", Toast.LENGTH_SHORT).show()
                // Navigate to search results
            } else {
                Toast.makeText(this, "Enter search term", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupCategoryButtons() {
        findViewById<View>(R.id.btnGamingProducts)?.setOnClickListener {
            Toast.makeText(this, "Gaming Products", Toast.LENGTH_SHORT).show()
        }

        findViewById<View>(R.id.btnAdultProducts)?.setOnClickListener {
            Toast.makeText(this, "Adult Products", Toast.LENGTH_SHORT).show()
        }

        findViewById<View>(R.id.btnKidsProducts)?.setOnClickListener {
            Toast.makeText(this, "Kids Products", Toast.LENGTH_SHORT).show()
        }

        findViewById<View>(R.id.btnToysProducts)?.setOnClickListener {
            Toast.makeText(this, "Toys Products", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupBottomNavigation() {
        // Home (active)
        findViewById<View>(android.R.id.content).setOnClickListener {
            Toast.makeText(this, "Already on Home", Toast.LENGTH_SHORT).show()
        }

        // Other tabs
        val bottomNavItems = listOf("Search", "Orders", "Saved", "Me")
        // Add more bottom nav logic here
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
        val hours = findViewById<TextView>(R.id.tvTimerHours)
        val mins = findViewById<TextView>(R.id.tvTimerMins)
        val secs = findViewById<TextView>(R.id.tvTimerSecs)

        // Animate countdown (demo)
        animateTimerDigit(secs, (secs.text.toString().toInt() - 1).toString())
    }

    private fun animateTimerDigit(view: TextView, newValue: String) {
        val animator = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.8f, 1f)
        animator.duration = 200
        animator.start()
        view.text = newValue
    }

    // Logout functionality
    fun logout(view: View) {
        val intent = Intent(this, SplashActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}