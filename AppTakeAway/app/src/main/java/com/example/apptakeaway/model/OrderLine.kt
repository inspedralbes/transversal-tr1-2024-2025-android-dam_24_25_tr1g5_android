package com.example.apptakeaway.model

data class OrderLine(
    val id: Int,
    val orderID: Int,
    val productId: Int,
    val productCategory: Int,
    val productName: String,
    val productDescription: String,
    val productSize: String,
    val productPrice: String,
    val productImagePath: String,
    val productColor: String,
    val date: String
)
