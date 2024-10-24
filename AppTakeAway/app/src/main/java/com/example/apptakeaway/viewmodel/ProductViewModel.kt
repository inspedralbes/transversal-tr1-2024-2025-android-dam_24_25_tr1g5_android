package com.example.apptakeaway.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptakeaway.api.RetrofitClient
import com.example.apptakeaway.model.Product
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductViewModel : ViewModel() {
    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> get() = _products

    fun loadProducts() {
        RetrofitClient.apiService.getProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    response.body()?.let { productList ->
                        _products.value = productList // Actualiza LiveData con los productos obtenidos
                    } ?: run {
                        Log.e("ProductViewModel", "La respuesta es nula")
                        _products.value = emptyList() // Manejo de caso donde no hay productos
                    }
                } else {
                    Log.e("ProductViewModel", "Error en la respuesta: ${response.code()} - ${response.message()}")
                    _products.value = emptyList() // Manejo de errores de respuesta
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Log.e("ProductViewModel", "Error de red: ${t.message}")
                _products.value = emptyList() // Manejo de errores de red
            }
        })
    }

    fun filterProducts(query: String?) {
        // Implementa el filtrado de productos aquí si es necesario
        if (query.isNullOrEmpty()) {
            // Si no hay consulta, muestra todos los productos
            loadProducts() // O puedes mantener una lista original si la tienes almacenada
        } else {
            // Filtra los productos según el nombre
            val filteredProducts = _products.value?.filter {
                it.name.contains(query, ignoreCase = true)
            }
            _products.value = filteredProducts ?: emptyList()
        }
    }
}