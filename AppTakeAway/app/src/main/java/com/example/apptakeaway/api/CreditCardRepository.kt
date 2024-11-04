package com.example.apptakeaway.api // Paquete donde se encuentra el cliente Retrofit

import com.example.apptakeaway.model.CreditCard // Importa el modelo de datos CreditCard
import retrofit2.Call // Importa la interfaz Call para las peticiones API
import retrofit2.Callback // Importa la interfaz Callback para manejar respuestas asíncronas
import retrofit2.Response // Importa la clase Response para representar la respuesta de una petición

/*
 * La clase `CreditCardRepository` actúa como un intermediario entre la fuente de datos
 * (en este caso, una API) y la lógica de negocio. Se encarga de realizar
 * las peticiones de tarjetas de crédito a la API usando Retrofit, manejando la
 * respuesta para retornar los datos o el error correspondiente.
 */
class CreditCardRepository {
    private val apiService = RetrofitClient.apiService // Inicializa el servicio API usando RetrofitClient

    /*
     * Método para añadir una tarjeta de crédito a la API.
     * Recibe el objeto creditCard y dos funciones lambda para manejar la respuesta.
     */
    fun addCreditCard(creditCard: CreditCard, onSuccess: () -> Unit, onError: (String) -> Unit) {
        // Realiza la llamada POST a la API
        apiService.addCreditCard(creditCard).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    onSuccess() // Llama a onSuccess si la respuesta es exitosa
                } else {
                    onError("Error: ${response.code()}") // Maneja el error
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onError("Error de red: ${t.message}") // Maneja el error de red
            }
        })
    }
}
