package com.example.apptakeaway

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.apptakeaway.api.RetrofitClient
import com.example.apptakeaway.model.Order
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class OrderActivity : AppCompatActivity() {

    private val TAG = "OrderActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        // Recupera el userId del Intent
        val userId = intent.getIntExtra("userId", -1)

        // Verificar si el userId es válido
        if (userId != -1) {
            // Llamada para obtener los pedidos del usuario
            getOrdersForUser(userId)
        } else {
            Toast.makeText(this, "User ID inválido", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun getOrdersForUser(userId: Int) {
        RetrofitClient.apiService.getOrdersForUser(userId).enqueue(object : Callback<List<Order>> {
            override fun onResponse(call: Call<List<Order>>, response: Response<List<Order>>) {
                if (response.isSuccessful) {
                    val orders = response.body()
                    if (!orders.isNullOrEmpty()) {
                        displayOrderDetails(orders[0]) // Muestra el primer pedido
                    } else {
                        Toast.makeText(this@OrderActivity, "No hay pedidos para este usuario", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e(TAG, "Error en la respuesta: ${response.errorBody()?.string()}")
                    Toast.makeText(this@OrderActivity, "Error al obtener pedidos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Order>>, t: Throwable) {
                Log.e(TAG, "Error de red: ${t.message}")
                Toast.makeText(this@OrderActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayOrderDetails(order: Order) {
        // Formatea la fecha
        val dateFormatted = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(order.date)

        // Referencias a las vistas de texto en activity_order.xml
        findViewById<TextView>(R.id.idTextView).text = "ID: ${order.id}"
        findViewById<TextView>(R.id.totalTextView).text = "Total: ${order.total ?: 0.0}"
        findViewById<TextView>(R.id.userIdTextView).text = "User ID: ${order.userId ?: "N/A"}"
        findViewById<TextView>(R.id.dateTextView).text = "Fecha: $dateFormatted"
        findViewById<TextView>(R.id.statusTextView).text = "Estado: ${order.status}"
        findViewById<TextView>(R.id.payTextView).text = "Pagado: ${if (order.pay) "Sí" else "No"}"
    }
}
