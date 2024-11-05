// OrderDetails.kt
package com.example.apptakeaway.model

import java.util.Date

data class Order(
    val id: Int,
    val total: Double?,
    val userId: Int?,
    val date: Date,
    val status: String,
    val pay: Int,
)

// Define los modelos de datos para Order, OrderLine y Product si aún no están definidos.
