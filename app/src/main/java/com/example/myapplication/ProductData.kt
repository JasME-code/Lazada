package com.example.myapplication

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: Int = 0,
    val name: String = "",
    val price: Double = 0.0,
    val rating: Float = 0f,
    val seller: String = "",
    val imageRes: Int = 0,
    val category: String = "",
    val stock: Int = 0,
    val material: String = "",
    val usage: String = "",
    val details: List<String> = emptyList(),
    val isRestricted: Boolean = false
) : Parcelable

@Parcelize
data class Order(
    val orderId: String,
    val items: List<Product>,
    val totalPrice: Double,
    val address: String,
    val paymentMethod: String,
    val date: String,
    var status: String = "Pending" // Options: Pending, Shipped, Delivered
) : Parcelable

// This acts as your "Database" for the session
object OrderManager {
    val orderHistory = mutableListOf<Order>()
}

object SellerManager {
    var totalSales: Double = 0.0
    var itemsSold: Int = 0
}