package com.example.apptakeaway.model

data class OrderRequest(
    val total: Total,
    val products: List<ProductOrder>
)

data class Total(
    val totalPrice: String,
    val userId: Int?, // Cambia Int a Int? para permitir valores nulos
    val pay: Int
)

data class ProductOrder(
    val id: Int,
    val price: String, // Utiliza String para el precio
    val quantity: Int
)
