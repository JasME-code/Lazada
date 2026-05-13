package com.example.myapplication

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val name: String,
    val email: String,
    val age: Int,
    val accountType: String // "Customer" or "Seller"
) : Parcelable