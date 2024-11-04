package com.example.apptakeaway.model

data class CreditCard(
    val userId: Int,
    val cardName: String,
    val cardNumber: String,
    val expirationDate: String,
    val cvv: String
)