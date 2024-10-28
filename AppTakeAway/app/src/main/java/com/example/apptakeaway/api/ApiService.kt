package com.example.apptakeaway.api // Paquete donde se encuentra la interfaz para el servicio API

/*
 * La interfaz `ApiService` define las llamadas a la API que se utilizarán para obtener los productos
 * de la aplicación. Utiliza Retrofit para manejar las solicitudes HTTP.
 * En este caso, se define un método para obtener una lista de productos desde el endpoint "productUser".
 */

import com.example.apptakeaway.model.Product // Importa el modelo Product que representa un producto
import retrofit2.Call // Clase de Retrofit para manejar respuestas de API
import retrofit2.http.GET // Anotación de Retrofit para las solicitudes GET

// Interfaz que define los servicios API
interface ApiService {
    // Método para obtener la lista de productos
    @GET("productUser") // Define la URL del endpoint
    fun getProducts(): Call<List<Product>> // Retorna una llamada que contiene una lista de productos
}
