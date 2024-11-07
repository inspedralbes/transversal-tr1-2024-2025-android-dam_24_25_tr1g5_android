package com.example.apptakeaway

import OrderAdapter
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apptakeaway.api.RetrofitClient
import com.example.apptakeaway.api.SocketManager
import com.example.apptakeaway.model.Order
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.socket.emitter.Emitter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderActivity : AppCompatActivity() {
    private val socket = SocketManager.getSocket()
    private lateinit var orderAdapter: OrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        // Configura el RecyclerView
        val ordersRecyclerView = findViewById<RecyclerView>(R.id.ordersRecyclerView)
        ordersRecyclerView.layoutManager = LinearLayoutManager(this)

        socket.connect()
        socket.on("orders", updateOrders)

        val userId = intent.getIntExtra("userId", -1)
        loadOrdersForUser(userId)
        setupBackButton()
    }

    private fun setupBackButton() {
        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            finish()
        }
    }

    private fun displayOrders(orders: List<Order>, userId: Int) {
        // Filtrar las órdenes para que solo se muestren las del usuario específico
        val userOrders = orders.filter { order -> order.userId == userId }

        if (userOrders.isEmpty()) {
            Toast.makeText(this, "No se encontraron órdenes para este usuario", Toast.LENGTH_SHORT)
                .show()
            return
        }

        // Configura el adaptador con las órdenes filtradas
        orderAdapter = OrderAdapter(userOrders)
        findViewById<RecyclerView>(R.id.ordersRecyclerView).adapter = orderAdapter
    }

    private fun loadOrdersForUser(userId: Int) {
        if (userId == -1) {
            Toast.makeText(this, "ID de usuario no válido", Toast.LENGTH_SHORT).show()
            return
        }

        val call = RetrofitClient.apiService.getOrdersForUser(userId)
        call.enqueue(object : Callback<List<Order>> {
            override fun onResponse(call: Call<List<Order>>, response: Response<List<Order>>) {
                if (response.isSuccessful) {
                    val orders = response.body()
                    if (orders != null) {
                        displayOrders(orders, userId) // Filtrar por userId específico
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

    // Llamada en el Listener del socket
    private val updateOrders = Emitter.Listener { args ->
        val data = args[0] as String
        val gson = Gson()
        val listType = object : TypeToken<List<Order>>() {}.type
        val orders: List<Order> = gson.fromJson(data, listType)

        // Asegurarse de pasar el userId al llamar displayOrders
        val userId = intent.getIntExtra("userId", -1)
        runOnUiThread { displayOrders(orders, userId) } // Filtrar por userId en el hilo principal
    }
}
