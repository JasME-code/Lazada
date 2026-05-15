package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
// --- FIREBASE IMPORTS ---
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener



val database = FirebaseDatabase.getInstance().getReference("products")

class Dashboard : AppCompatActivity() {

    private val masterList = mutableListOf<Product>()
    private var displayList = mutableListOf<Product>()
    private lateinit var productAdapter: ProductAdapter
    private var userAge: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard)

        // 1. Extract User Data
        val user = intent.getParcelableExtra<User>("USER_DATA")
        userAge = user?.age ?: 0
        val role = user?.role ?: "Customer"

        // 2. Setup UI
        setupProductList()
        setupClickListeners(role)
        setupSearch()

        // 3. Connect to Firebase
        syncWithFirebase()
    }

    private fun syncWithFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                masterList.clear()

                if (!snapshot.exists()) {
                    // Cloud is empty, upload defaults
                    uploadDefaultsToFirebase()
                } else {
                    // Cloud has data, download it
                    for (item in snapshot.children) {
                        val p = item.getValue(Product::class.java)
                        if (p != null) masterList.add(p)
                    }
                    filterAndRefreshList()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Dashboard, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun uploadDefaultsToFirebase() {
        val defaults = listOf(
            Product(1, "Nike Pegasus 42", 5999.0, 4.9f, "Nike Official", R.drawable.img, "Shoes", 15, "Mesh", "Running", listOf("Breathable"), false),
            Product(2, "Gaming Laptop", 45000.0, 4.8f, "TechShop", R.drawable.img, "Tech", 5, "Aluminium", "Gaming", listOf("RTX 4060"), false),
            Product(3, "Special Item (18+)", 1200.0, 4.2f, "AdultStore", R.drawable.img, "Misc", 2, "Varies", "Usage", listOf("Restricted"), true)
        )
        for (p in defaults) {
            database.child(p.id.toString()).setValue(p)
        }
    }

    private fun setupProductList() {
        val lvFlash = findViewById<ListView>(R.id.listViewFlashSale)
        productAdapter = ProductAdapter(this, displayList)
        lvFlash.adapter = productAdapter

        lvFlash.setOnItemClickListener { _, _, position, _ ->
            val selectedProduct = displayList[position]
            CartManager.cartList.add(selectedProduct)
            Toast.makeText(this, "Added ${selectedProduct.name} to Cart!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun filterAndRefreshList() {
        val filtered = if (userAge < 18) {
            masterList.filter { !it.isRestricted }
        } else {
            masterList.toList()
        }

        displayList.clear()
        displayList.addAll(filtered)
        productAdapter.notifyDataSetChanged()
    }

    private fun setupClickListeners(role: String) {
        findViewById<Button>(R.id.btnSortPrice)?.setOnClickListener {
            displayList.sortBy { it.price }
            productAdapter.notifyDataSetChanged()
        }

        val btnSeller = findViewById<Button>(R.id.btnSellerCenter)
        if (role == "Seller") {
            btnSeller?.visibility = View.VISIBLE
            btnSeller?.setOnClickListener {
                startActivity(Intent(this, SellerDashboardActivity::class.java))
            }
        }

        findViewById<View>(R.id.btnCartIcon)?.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
    }

    private fun setupSearch() {
        val et = findViewById<TextInputEditText>(R.id.etSearchFull)
        findViewById<View>(R.id.btnSearchFull)?.setOnClickListener {
            val query = et?.text.toString().trim().lowercase()
            val searched = masterList.filter { product ->
                product.name.lowercase().contains(query) && (if (userAge < 18) !product.isRestricted else true)
            }
            displayList.clear()
            displayList.addAll(searched)
            productAdapter.notifyDataSetChanged()
        }
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
            view.findViewById<TextView>(android.R.id.text2).text = "₱${"%.2f".format(item.price)} • Stock: ${item.stock}"
            return view
        }
    }
}