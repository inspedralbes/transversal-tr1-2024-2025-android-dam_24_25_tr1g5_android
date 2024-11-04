package com.example.apptakeaway.model

data class OrderRequest(
    val total: Total,
    val products: List<ProductOrder>
)

data class Total(
    val totalPrice: String, // Utiliza String según tu ejemplo JSON
    val userId: Int,
    val pay: Int // Cambia a Boolean si prefieres usar true/false, pero aquí lo dejé como Int para que coincida con tu descripción
)

data class ProductOrder(
    val id: Int,
    val price: String, // Utiliza String para el precio
    val quantity: Int
)
