// OrderDetails.kt
package com.example.apptakeaway.model

import java.util.Date
data class Order(
    val id: Int,
    val total: String,
    val userId: Int,
    var status: String,
    val pay: Int,
    val date: String,
    val dateStart: String?,
    val dateReady: String?,
    val dateEnd: String?,
    val productCount: Int,
    val orderLines: List<OrderLine>
)


// Define los modelos de datos para Order, OrderLine y Product si aún no están definidos.
