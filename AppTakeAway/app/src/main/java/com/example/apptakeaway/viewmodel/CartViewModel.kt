package com.example.apptakeaway.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.apptakeaway.model.CartItem
import com.example.apptakeaway.model.Product

class CartViewModel : ViewModel() {
    private val _cartItems = MutableLiveData<List<CartItem>>()
    val cartItems: LiveData<List<CartItem>> get() = _cartItems

    init {
        if (_cartItems.value == null) {
            _cartItems.value = emptyList()
        }
    }

    fun addToCart(product: Product) {
        val currentList = _cartItems.value?.toMutableList() ?: mutableListOf()
        val existingItem = currentList.find { it.product.id == product.id }

        if (existingItem != null) {
            existingItem.quantity++
        } else {
            currentList.add(CartItem(product, 1))
        }

        _cartItems.value = currentList
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
            _cartItems.value = currentList
        }
    }

    fun updateItemActiveState(cartItem: CartItem, isActive: Boolean) {
        val currentList = _cartItems.value?.toMutableList() ?: mutableListOf()
        val itemToUpdate = currentList.find { it.product.id == cartItem.product.id }

        if (itemToUpdate != null) {
            itemToUpdate.isActive = isActive
            _cartItems.value = currentList
        }
    }

    fun getCartTotal(): Double {
        return _cartItems.value?.filter { it.isActive }?.sumOf { it.product.price * it.quantity } ?: 0.0
    }
}