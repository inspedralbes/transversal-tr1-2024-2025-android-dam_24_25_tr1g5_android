package com.example.apptakeaway.viewmodel // Paquete donde se encuentra el ViewModel del carrito

/*
 * La clase `CartViewModel` es responsable de manejar la lógica relacionada con el carrito de compras
 * en la aplicación. Extiende `ViewModel` para preservar los datos durante cambios de configuración,
 * como la rotación de pantalla. Contiene listas de productos disponibles y de ítems en el carrito,
 * así como métodos para añadir, actualizar y eliminar productos en el carrito,
 * así como calcular el total del carrito.
 */

import androidx.lifecycle.LiveData // Clase para representar datos que pueden ser observados
import androidx.lifecycle.MutableLiveData // Clase para representar datos observables que pueden ser modificados
import androidx.lifecycle.ViewModel // Clase base para los ViewModels
import com.example.apptakeaway.model.CartItem // Modelo para los ítems del carrito
import com.example.apptakeaway.model.Product // Modelo para los productos

// Clase ViewModel que maneja la lógica del carrito de compras
class CartViewModel : ViewModel() {


    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val _cartItems = MutableLiveData<List<CartItem>>() // Cambiado a privado para mantener la encapsulación
    val cartItems: LiveData<List<CartItem>> = _cartItems

    // Nueva lista para los elementos a pagar
    private val _payItems = MutableLiveData<List<CartItem>>()
    val payItems: LiveData<List<CartItem>> = _payItems

    init {
        _products.value = emptyList()
        _cartItems.value = emptyList()
        _payItems.value = emptyList() // Inicializa payItems como una lista vacía
    }

    fun setProducts(productList: List<Product>) {
        _products.value = productList
    }
    fun addToCart(product: Product) {
        val currentCart = _cartItems.value?.toMutableList() ?: mutableListOf()
        val existingItem = currentCart.find { it.product.id == product.id }

        if (existingItem != null) {
            existingItem.quantity++
        } else {
            val newItem = CartItem(product, 1) // Crear un nuevo CartItem
            currentCart.add(newItem) // Añadir al carrito

        }

        // Usar postValue en lugar de setValue
        _cartItems.postValue(currentCart.toList())
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

            _cartItems.postValue(currentList.toList())
        }
    }

    fun removeFromCart(cartItem: CartItem) {
        val currentCart = _cartItems.value?.toMutableList() ?: mutableListOf()
        currentCart.remove(cartItem)
        _cartItems.postValue(currentCart.toList())
    }

    // Métodos para manejar payItems
    fun addPayItem(cartItem: CartItem) {
        val currentPayItems = _payItems.value?.toMutableList() ?: mutableListOf()
        currentPayItems.add(cartItem)
        _payItems.postValue(currentPayItems)
    }

    fun removePayItem(cartItem: CartItem) {
        val currentPayItems = _payItems.value?.toMutableList() ?: mutableListOf()
        currentPayItems.remove(cartItem)
        _payItems.postValue(currentPayItems)
    }

    fun clearPayItems() {
        _payItems.postValue(emptyList()) // Limpiar la lista de elementos a pagar
    }

    fun getCartTotal(): Double {
        return _cartItems.value
            ?.filter { it.isSelected }
            ?.sumOf { it.product.price.toDouble() * it.quantity } ?: 0.0
    }

    fun updateItemSelection(cartItem: CartItem) {
        val updatedCartItems = _cartItems.value?.map {
            if (it.product.id == cartItem.product.id) {
                it.copy(isSelected = cartItem.isSelected)
            } else it
        }

        updatedCartItems?.let {
            _cartItems.postValue(it)
        }
    }
    fun removeUnselectedPayItems() {
        // Filtra solo los elementos seleccionados en payItems y actualiza la lista
        val selectedItems = _payItems.value?.filter { it.isSelected } ?: emptyList()
        _payItems.postValue(selectedItems)
    }

    fun addItemsToPayItems() {
        // Obtener la lista actual de `cartItems` para sincronizar
        val currentCartItems = _cartItems.value ?: emptyList()

        // Crear una nueva lista con solo los ítems seleccionados en `cartItems`
        val selectedItems = currentCartItems.filter { it.isSelected }

        // Actualizar `payItems` solo con los elementos seleccionados
        _payItems.postValue(selectedItems)
    }



    fun hasSelectedItems(): Boolean {
        return _cartItems.value?.any { it.isSelected } ?: false
    }
}
