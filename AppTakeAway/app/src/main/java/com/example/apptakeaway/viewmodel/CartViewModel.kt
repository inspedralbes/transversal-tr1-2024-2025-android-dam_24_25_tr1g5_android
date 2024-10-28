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

    // Lista de productos (disponibles en la tienda)
    private val _products = MutableLiveData<List<Product>>() // MutableLiveData para productos
    val products: LiveData<List<Product>> = _products // LiveData para observación externa

    // Lista de ítems en el carrito (independiente de los productos de la tienda)
    private val _cartItems = MutableLiveData<List<CartItem>>() // MutableLiveData para ítems del carrito
    val cartItems: LiveData<List<CartItem>> = _cartItems // LiveData para observación externa

    // Inicializa las listas de productos y del carrito
    init {
        _products.value = emptyList() // Inicializa productos vacíos o añade productos de prueba aquí
        _cartItems.value = emptyList() // Inicializa el carrito vacío
    }

    // Método para establecer productos en la tienda (puedes reemplazarlo por una llamada de API)
    fun setProducts(productList: List<Product>) {
        _products.value = productList // Actualiza la lista de productos
    }

    // Método para añadir un producto al carrito
    fun addToCart(product: Product) {
        // Obtiene la lista actual del carrito o crea una nueva si es nula
        val currentCart = _cartItems.value?.toMutableList() ?: mutableListOf()
        // Busca si el producto ya está en el carrito
        val existingItem = currentCart.find { it.product.id == product.id }

        if (existingItem != null) {
            // Si existe, incrementa la cantidad
            existingItem.quantity++
        } else {
            // Si no existe, añade un nuevo CartItem
            currentCart.add(CartItem(product, 1))
        }

        // Notifica los cambios en el carrito
        _cartItems.value = currentCart.toList()
    }

    // Método para actualizar la cantidad de un ítem en el carrito
    fun updateItemQuantity(cartItem: CartItem, newQuantity: Int) {
        // Obtiene la lista actual del carrito o crea una nueva si es nula
        val currentList = _cartItems.value?.toMutableList() ?: mutableListOf()
        // Busca el ítem que se quiere actualizar
        val itemToUpdate = currentList.find { it.product.id == cartItem.product.id }

        if (itemToUpdate != null) {
            if (newQuantity > 0) {
                // Actualiza la cantidad si es mayor que 0
                itemToUpdate.quantity = newQuantity
            } else {
                // Remueve el ítem si la cantidad es 0
                currentList.remove(itemToUpdate)
            }
            // Fuerza un reset temporal de la lista del carrito
            _cartItems.value = emptyList()
            // Notifica los cambios en el carrito
            _cartItems.value = currentList.toList()
        }
    }

    // Método para eliminar un ítem del carrito
    fun removeFromCart(cartItem: CartItem) {
        // Obtiene la lista actual del carrito o crea una nueva si es nula
        val currentCart = _cartItems.value?.toMutableList() ?: mutableListOf()
        // Remueve el ítem especificado
        currentCart.remove(cartItem)
        // Notifica los cambios en el carrito
        _cartItems.value = currentCart.toList()
    }

    // Método para calcular el total del carrito
    fun getCartTotal(): Double {
        // Solo suma el precio de los productos seleccionados
        return _cartItems.value
            ?.filter { it.isSelected }  // Filtra solo los elementos seleccionados
            ?.sumOf { it.product.price.toDouble() * it.quantity } ?: 0.0
    }

    fun updateItemSelection(cartItem: CartItem) {
        // Actualiza el estado del ítem seleccionado en la lista del carrito
        _cartItems.value = _cartItems.value?.map {
            if (it.product.id == cartItem.product.id) {
                it.copy(isSelected = cartItem.isSelected)
            } else it
        }
        // Actualiza la lista de observadores del carrito
        _cartItems.value = _cartItems.value
    }
}
