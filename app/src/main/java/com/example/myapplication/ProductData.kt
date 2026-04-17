package com.example.myapplication

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val rating: Float,
    val seller: String,
    val imageRes: Int = R.drawable.img
)