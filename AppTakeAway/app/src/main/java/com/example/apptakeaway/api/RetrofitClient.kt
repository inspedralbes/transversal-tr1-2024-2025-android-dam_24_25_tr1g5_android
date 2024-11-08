package com.example.apptakeaway.api // Paquete donde se encuentra el cliente Retrofit

/*
 * La clase `RetrofitClient` es un objeto singleton que configura y proporciona una instancia de Retrofit
 * para realizar solicitudes a la API. Define la URL base para las solicitudes y especifica el convertidor
 * de JSON que se utilizará para deserializar las respuestas de la API.
 * La instancia de `ApiService` se crea a partir de Retrofit para permitir el acceso a las funciones de la API.
 */

import retrofit2.Retrofit // Clase principal de Retrofit para crear instancias
import retrofit2.converter.gson.GsonConverterFactory // Convertidor de Gson para convertir JSON a objetos

// Objeto singleton que maneja la configuración de Retrofit
object RetrofitClient {
    // URL base de la API
    private const val BASE_URL = "http://192.168.1.20:3000/"

    // Instancia de Retrofit creada de manera perezosa
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL) // Configura la URL base
            .addConverterFactory(GsonConverterFactory.create()) // Añade el convertidor de Gson
            .build() // Construye la instancia de Retrofit
    }
    // Proporciona la implementación de ApiService de manera perezosa
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java) // Crea una instancia de ApiService
    }
}
