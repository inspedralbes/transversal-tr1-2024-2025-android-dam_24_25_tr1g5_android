package com.example.apptakeaway.api // Paquete donde se encuentra el cliente Retrofit

import com.example.apptakeaway.model.OrderRequest // Importa el modelo de datos OrderRequest
import com.example.apptakeaway.model.OrderResponse // Importa el modelo de respuesta OrderResponse (deberías definirlo si no lo tienes)
import retrofit2.Call // Importa la interfaz Call para las peticiones API
import retrofit2.Callback // Importa la interfaz Callback para manejar respuestas asíncronas
import retrofit2.Response // Importa la clase Response para representar la respuesta de una petición

/*
 * La clase `OrderRepository` actúa como un intermediario entre la fuente de datos
 * (en este caso, una API) y la lógica de negocio. Se encarga de realizar
 * las peticiones de pedidos a la API usando Retrofit, manejando la
 * respuesta para retornar los datos o el error correspondiente.
 */
class OrderRepository {
    private val apiService = RetrofitClient.apiService // Inicializa el servicio API usando RetrofitClient

    /*
     * Método para enviar un pedido a la API.
     * Recibe el objeto orderRequest y dos funciones lambda para manejar la respuesta.
     */
    fun placeOrder(orderRequest: OrderRequest, onSuccess: (OrderResponse) -> Unit, onError: (String) -> Unit) {
        // Realiza la llamada POST a la API
        apiService.placeOrder(orderRequest).enqueue(object : Callback<OrderResponse> {
            override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { orderResponse ->
                        onSuccess(orderResponse) // Llama a onSuccess con la respuesta
                    } ?: onError("Resposta vuida del servidor")
                } else {
                    onError("Error: ${response.code()}") // Maneja el error
                }
            }

            override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                onError("Error de xarxa: ${t.message}") // Maneja el error de red
            }
        })
    }
}
