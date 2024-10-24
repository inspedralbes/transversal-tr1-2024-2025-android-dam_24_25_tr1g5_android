package com.example.apptakeaway.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.apptakeaway.model.CartItem
import com.example.apptakeaway.model.Product

class CartViewModel : ViewModel() {
    private val _cartItems = MutableLiveData<List<CartItem>>() // Cambiado a List<CartItem>
    val cartItems: LiveData<List<CartItem>> get() = _cartItems

    init {
        _cartItems.value = emptyList() // Inicializa la lista del carrito como vacía
    }

    fun addToCart(product: Product) {
        val currentList = _cartItems.value?.toMutableList() ?: mutableListOf()
        val existingItem = currentList.find { it.product.id == product.id }

        if (existingItem != null) {
            existingItem.quantity++ // Incrementa la cantidad si ya existe
        } else {
            currentList.add(CartItem(product, 1)) // Añade un nuevo item al carrito
        }

        _cartItems.value = currentList // Notifica a los observadores
    }

    fun updateItemQuantity(cartItem: CartItem, newQuantity: Int) {
        val currentList = _cartItems.value?.toMutableList() ?: mutableListOf()
        val itemToUpdate = currentList.find { it.product.id == cartItem.product.id }

        if (itemToUpdate != null) {
            if (newQuantity > 0) {
                itemToUpdate.quantity = newQuantity
            } else {
                currentList.remove(itemToUpdate)
            }
            _cartItems.value = currentList // Notifica a los observadores
        }
    }

    fun updateItemActiveState(cartItem: CartItem, isActive: Boolean) {
        val currentList = _cartItems.value?.toMutableList() ?: mutableListOf()
        val itemToUpdate = currentList.find { it.product.id == cartItem.product.id }

        if (itemToUpdate != null) {
            itemToUpdate.isActive = isActive
            _cartItems.value = currentList // Notifica a los observadores
        }
    }

    fun getCartTotal(): Double {
        return _cartItems.value?.sumOf { it.product.price.toDouble() * it.quantity } ?: 0.0
    }
}