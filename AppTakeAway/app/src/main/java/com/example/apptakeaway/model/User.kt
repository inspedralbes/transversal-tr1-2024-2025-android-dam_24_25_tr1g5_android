// User.kt
package com.example.apptakeaway.model

data class User(
    val id: Int,
    val email: String,
    val password: String,
    val firstName: String?,
    val lastName: String?,
    val typeUserId: Int?,
    val paymentMethod: String?
)

