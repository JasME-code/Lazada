package com.example.myapplication

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val rating: Float,
    val seller: String,
    val imageRes: Int,
    val category: String,
    val stock: Int,
    val description: String,
    val specs: String,
    val reviews: List<String>,
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