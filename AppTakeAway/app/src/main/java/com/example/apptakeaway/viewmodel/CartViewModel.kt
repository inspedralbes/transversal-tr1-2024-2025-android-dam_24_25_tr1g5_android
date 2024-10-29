package com.example.apptakeaway.viewmodel // Paquete donde se encuentra el ViewModel del carrito

/*
 * La clase `CartViewModel` es responsable de manejar la lógica relacionada con el carrito de compras
 * en la aplicación. Extiende `ViewModel` para preservar los datos durante cambios de configuración,
 * como la rotación de pantalla. Contiene listas de productos disponibles y de ítems en el carrito,
 * así como métodos para añadir, actualizar y eliminar productos en el carrito,
 * así como calcular el total del carrito.
 */

import android.util.Log // Importar la clase Log para registros (aunque no se usa aquí)
import androidx.lifecycle.LiveData // Clase para representar datos que pueden ser observados
import androidx.lifecycle.MutableLiveData // Clase para representar datos observables que pueden ser modificados
import androidx.lifecycle.ViewModel // Clase base para los ViewModels
import com.example.apptakeaway.model.CartItem // Modelo para los ítems del carrito
import com.example.apptakeaway.model.Product // Modelo para los productos

// Clase ViewModel que maneja la lógica del carrito de compras
class CartViewModel : ViewModel() {

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val _cartItems = MutableLiveData<List<CartItem>>()
    val cartItems: LiveData<List<CartItem>> = _cartItems

    init {
        _products.value = emptyList()
        _cartItems.value = emptyList()
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
            currentCart.add(CartItem(product, 1))
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

            // Evitar asignación temporal a emptyList y usar postValue
            _cartItems.postValue(currentList.toList())
        }
    }

    fun removeFromCart(cartItem: CartItem) {
        val currentCart = _cartItems.value?.toMutableList() ?: mutableListOf()
        currentCart.remove(cartItem)
        _cartItems.postValue(currentCart.toList())
    }

    fun getCartTotal(): Double {
        return _cartItems.value
            ?.filter { it.isSelected }
            ?.sumOf { it.product.price.toDouble() * it.quantity } ?: 0.0
    }

    fun updateItemSelection(cartItem: CartItem) {
        // Mapea la lista actual, actualizando solo el ítem que coincide con el `id` del producto
        val updatedCartItems = _cartItems.value?.map {
            if (it.product.id == cartItem.product.id) {
                it.copy(isSelected = cartItem.isSelected)
            } else it
        }

        // Publica los cambios en `_cartItems` solo si `updatedCartItems` no es nulo
        updatedCartItems?.let {
            _cartItems.postValue(it)
        }
    }

}