package com.example.apptakeaway.api // Paquete donde se encuentra la interfaz para el servicio API

/*
 * La interfaz `ApiService` define las llamadas a la API que se utilizarán para obtener los productos
 * de la aplicación. Utiliza Retrofit para manejar las solicitudes HTTP.
 * En este caso, se define un método para obtener una lista de productos desde el endpoint "productUser".
 */

import com.example.apptakeaway.model.Login
import com.example.apptakeaway.model.Order
import com.example.apptakeaway.model.Product // Importa el modelo Product que representa un producto
import com.example.apptakeaway.model.User
import org.mindrot.jbcrypt.BCrypt
import retrofit2.Call // Clase de Retrofit para manejar respuestas de API
import retrofit2.http.GET // Anotación de Retrofit para las solicitudes GET
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path

// Interfaz que define los servicios API
interface ApiService {
    // Método para obtener la lista de productos
    @GET("productUser") // Define la URL del endpoint
    fun getProducts(): Call<List<Product>> // Retorna una llamada que contiene una lista de productos

    @POST("login")
    fun login(@Body loginRequest: Login): Call<User>

    @POST("user")
    fun postUser(@Body user: User): Call<Void>

    @GET("orders/user/{userId}")
    fun getOrdersForUser(@Path("userId") userId: Int): Call<List<Order>>
}

