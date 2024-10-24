package com.example.apptakeaway.model

data class CartItem(val product: Product, var quantity: Int, var isActive: Boolean = true)