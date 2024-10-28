package com.example.apptakeaway.viewmodel // Paquete donde se encuentra el ViewModel de productos

/*
 * La clase `ProductViewModel` es responsable de manejar la lógica relacionada con los productos en la
 * aplicación. Extiende `ViewModel` para preservar los datos durante cambios de configuración, como la
 * rotación de pantalla. Esta clase se encarga de cargar productos desde la API, actualizar el estado
 * de los productos y proporcionar funcionalidad de filtrado para mostrar productos específicos según
 * las consultas del usuario.
 */

import android.util.Log // Importar la clase Log para registros
import androidx.lifecycle.LiveData // Clase para representar datos que pueden ser observados
import androidx.lifecycle.MutableLiveData // Clase para representar datos observables que pueden ser modificados
import androidx.lifecycle.ViewModel // Clase base para los ViewModels
import androidx.lifecycle.viewModelScope // Coroutines para la gestión de tareas asíncronas
import com.example.apptakeaway.api.RetrofitClient // Cliente Retrofit para llamadas a la API
import com.example.apptakeaway.model.Product // Modelo para los productos
import kotlinx.coroutines.launch // Importar para lanzar corutinas
import retrofit2.Call // Clase de Retrofit para manejar llamadas
import retrofit2.Callback // Interfaz para manejar respuestas asíncronas de la API
import retrofit2.Response // Clase para las respuestas de la API

// Clase ViewModel que maneja la lógica de los productos
class ProductViewModel : ViewModel() {
    // Lista mutable de productos que se observará
    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> get() = _products // LiveData para observación externa

    // Método para cargar productos desde la API
    fun loadProducts() {
        // Realiza la llamada a la API para obtener productos
        RetrofitClient.apiService.getProducts().enqueue(object : Callback<List<Product>> {
            // Manejo de la respuesta exitosa de la API
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) { // Verifica si la respuesta fue exitosa
                    response.body()?.let { productList ->
                        _products.value = productList // Actualiza LiveData con los productos obtenidos
                    } ?: run { // Manejo del caso donde la respuesta es nula
                        Log.e("ProductViewModel", "La respuesta es nula")
                        _products.value = emptyList() // Inicializa la lista de productos como vacía
                    }
                } else { // Manejo de errores de respuesta
                    Log.e("ProductViewModel", "Error en la respuesta: ${response.code()} - ${response.message()}")
                    _products.value = emptyList() // Inicializa la lista de productos como vacía
                }
            }

            // Manejo de fallos en la llamada a la API
            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Log.e("ProductViewModel", "Error de red: ${t.message}") // Registra el error de red
                _products.value = emptyList() // Inicializa la lista de productos como vacía
            }
        })
    }

    // Método para filtrar productos según una consulta
    fun filterProducts(query: String?) {
        // Implementa el filtrado de productos aquí si es necesario
        if (query.isNullOrEmpty()) { // Verifica si la consulta está vacía
            loadProducts() // Vuelve a cargar todos los productos
        } else {
            // Filtra los productos según el nombre
            val filteredProducts = _products.value?.filter {
                it.name.contains(query, ignoreCase = true) // Filtra los productos que contienen la consulta
            }
            _products.value = filteredProducts ?: emptyList() // Actualiza la lista de productos filtrados
        }
    }
}
