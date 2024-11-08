package com.example.apptakeaway.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptakeaway.api.OrderRepository
import com.example.apptakeaway.model.CartItem
import com.example.apptakeaway.model.OrderRequest
import com.example.apptakeaway.model.ProductOrder
import com.example.apptakeaway.model.Total
import kotlinx.coroutines.launch

class PayViewModel: ViewModel() {

    private val _payItems = MutableLiveData<List<CartItem>>()
    val payItems: LiveData<List<CartItem>> = _payItems

    private val _cartItems = MutableLiveData<List<CartItem>>() // Cambiado a privado para mantener la encapsulación
    val cartItems: LiveData<List<CartItem>> = _cartItems

    private val orderRepository = OrderRepository()

    fun clearPayItems() {
        _payItems.postValue(emptyList()) // Limpiar la lista de elementos a pagar
    }
    fun addItemsToPayItems() {
        // Obtener la lista actual de `cartItems` para sincronizar
        val currentCartItems = _cartItems.value ?: emptyList()

        // Crear una nueva lista con solo los ítems seleccionados en `cartItems`
        val selectedItems = currentCartItems.filter { it.isSelected }

        // Actualizar `payItems` solo con los elementos seleccionados
        _payItems.postValue(selectedItems)
    }
}
