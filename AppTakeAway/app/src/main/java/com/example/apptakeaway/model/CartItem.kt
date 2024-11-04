package com.example.apptakeaway.model // Paquete donde se encuentra el modelo del ítem del carrito

/*
 * La clase de datos `CartItem` representa un ítem en el carrito de compras de la aplicación.
 * Contiene información sobre el producto asociado, la cantidad de ese producto en el carrito y un estado de selección
 * que indica si el ítem está seleccionado o no. Esta clase se utiliza para manejar los productos que el usuario
 * ha agregado a su carrito durante el proceso de compra.
 */

import java.io.Serializable

data class CartItem(
    val product: Product,
    var quantity: Int,
    var isSelected: Boolean = true
) : Serializable
