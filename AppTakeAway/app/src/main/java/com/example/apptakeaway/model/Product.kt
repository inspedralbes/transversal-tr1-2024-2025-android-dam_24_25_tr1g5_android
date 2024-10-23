package com.example.apptakeaway.model

data class Product(
    val id: Int,
    val categoryId: Int,
    val name: String,
    val description: String,
    val size: String,
    val price: Double,
    val imagePath: String,
    val colors: String,
    val stock: Int,
    val activated: Boolean
)