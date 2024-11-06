package com.example.apptakeaway.viewmodel

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.apptakeaway.adapter.ProductAdapter
import com.example.apptakeaway.model.Product
import com.example.apptakeaway.api.SocketManager

class ProductViewModel : ViewModel() {
    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> get() = _products

    private fun observeSocketUpdates() {
        // Recibe los productos desde el servidor cuando se actualicen
        SocketManager.socket?.on("products") { args ->
            val updatedProducts = args[0] as? List<Product> ?: emptyList()
            _products.postValue(updatedProducts)  // Actualiza la lista de productos
        }
    }

    init {
        // Conectar al Socket cuando el ViewModel se inicializa
        SocketManager.connect()
        observeSocketUpdates()
        }


    // Desconectar el socket cuando el ViewModel se destruya
    override fun onCleared() {
        super.onCleared()
        SocketManager.disconnect()
    }


    // Método para filtrar productos según una consulta
    fun filterProducts(query: String?) {
        // Implementa el filtrado de productos aquí si es necesario
        if (query.isNullOrEmpty()) { // Verifica si la consulta está vacía
            observeSocketUpdates() // Vuelve a cargar todos los productos
        } else {
            // Filtra los productos según el nombre
            val filteredProducts = _products.value?.filter {
                it.name.contains(query, ignoreCase = true) // Filtra los productos que contienen la consulta
            }
            _products.value = filteredProducts ?: emptyList() // Actualiza la lista de productos filtrados
        }
    }

     fun socketloadProducts(adapter: ProductAdapter, owner: LifecycleOwner) {
        products.observe(owner, Observer { updatedProducts ->
            // Actualiza la UI, por ejemplo, RecyclerView con los productos
            // Si usas un RecyclerView, por ejemplo:
            adapter.submitList(updatedProducts)

        })
    }
}