package com.example.apptakeaway

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.apptakeaway.api.RetrofitClient // Asegúrate de importar RetrofitClient aquí
import com.example.apptakeaway.model.Order
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        // Recupera los datos del Intent
        val userId = intent.getIntExtra("userId", -1)

        // Llama a la función para cargar las órdenes del usuario
        loadOrdersForUser(userId)
    }

    private fun loadOrdersForUser(userId: Int) {
        // Verifica que el userId sea válido
        if (userId == -1) {
            Toast.makeText(this, "ID de usuario no válido", Toast.LENGTH_SHORT).show()
            return
        }

        // Llama a la API usando Retrofit
        val call = RetrofitClient.apiService.getOrdersForUser(userId)
        call.enqueue(object : Callback<List<Order>> {
            override fun onResponse(call: Call<List<Order>>, response: Response<List<Order>>) {
                if (response.isSuccessful) {
                    val orders = response.body()
                    if (orders != null) {
                        displayOrders(orders)
                    } else {
                        Toast.makeText(this@OrderActivity, "No se encontraron órdenes", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@OrderActivity, "Error en la respuesta: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Order>>, t: Throwable) {
                Log.e("OrderActivity", "Error de red", t)
                Toast.makeText(this@OrderActivity, "Error al cargar órdenes", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayOrders(orders: List<Order>) {
        // Muestra las órdenes en el layout
        val ordersTextView = findViewById<TextView>(R.id.ordersTextView)
        val ordersText = orders.joinToString(separator = "\n\n") { order ->
            "Orden ID: ${order.id}\n" +
                    "Fecha: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(order.date)}\n" +
                    "Total: ${order.total}\n" +
                    "Estado: ${order.status}"
        }
        ordersTextView.text = ordersText
    }
}
