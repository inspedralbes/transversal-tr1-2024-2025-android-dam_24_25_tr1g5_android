package com.example.apptakeaway.model

data class PayItem(
    val product: Product, // Referencia al producto asociado al ítem del carrito
    var quantity: Int, // Cantidad del producto en el carrito

)