package com.example.apptakeaway.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.apptakeaway.model.PayItem
import com.example.apptakeaway.model.CartItem

class PayViewModel : ViewModel() {

    private val _payItems = MutableLiveData<List<PayItem>>()
    val payItems: LiveData<List<PayItem>> = _payItems

    init {
        _payItems.value = emptyList()
    }

    fun transferSelectedItems(cartItems: List<CartItem>) {
        val selectedPayItems = cartItems.filter { it.isSelected }
            .map { PayItem(it.product, it.quantity) } // Mapeo a PayItem

        _payItems.value = selectedPayItems // Asigna los ítems seleccionados
        logPayItems(selectedPayItems) // Llama a la función para registrar los ítems
    }

    private fun logPayItems(payItems: List<PayItem>) {
        // Usa Logcat para imprimir la lista de PayItem
        payItems.forEach {
            // Registra cada PayItem en el Logcat
            Log.d("PayViewModel", "PayItem: Product - ${it.product.price}, Quantity - ${it.quantity}")
        }
    }
    fun getPayTotal(): Double {
        return _payItems.value
            ?.sumOf { it.product.price.toDouble() * it.quantity } ?: 0.0
    }
}
