package com.example.apptakeaway.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.apptakeaway.model.CartItem
import com.example.apptakeaway.model.Product

class CartViewModel : ViewModel() {
    private val _cartItems = MutableLiveData<List<CartItem>>()
    val cartItems: LiveData<List<CartItem>> = _cartItems

    init {
        _cartItems.value = emptyList()
    }

    fun addToCart(product: Product) {
        val currentList = _cartItems.value?.toMutableList() ?: mutableListOf()
        val existingItem = currentList.find { it.product.id == product.id }

        if (existingItem != null) {
            existingItem.quantity++
        } else {
            currentList.add(CartItem(product, 1))
        }

        _cartItems.value = currentList.toList()
        Log.d("CartViewModel", "Producto añadido/actualizado: ${product.name}, Cantidad: ${existingItem?.quantity ?: 1}")
    }

    fun updateItemQuantity(cartItem: CartItem, newQuantity: Int) {
        val currentList = _cartItems.value?.toMutableList() ?: mutableListOf()
        val itemToUpdate = currentList.find { it.product.id == cartItem.product.id }

        if (itemToUpdate != null) {
            itemToUpdate.quantity = newQuantity
            _cartItems.value = currentList.toList()
            Log.d("CartViewModel", "Cantidad actualizada para ${cartItem.product.name}: $newQuantity")
        }
    }

    fun removeFromCart(cartItem: CartItem) {
        val currentList = _cartItems.value?.toMutableList() ?: mutableListOf()
        currentList.remove(cartItem)
        _cartItems.value = currentList.toList()
        Log.d("CartViewModel", "Producto eliminado del carrito: ${cartItem.product.name}")
    }

    fun getCartTotal(): Double {
        return _cartItems.value?.sumOf { it.product.price.toDouble() * it.quantity } ?: 0.0
    }
}