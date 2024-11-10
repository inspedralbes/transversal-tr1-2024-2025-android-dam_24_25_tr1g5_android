package com.example.apptakeaway
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apptakeaway.R
import com.example.apptakeaway.adapter.OrderAdapter
import com.example.apptakeaway.api.RetrofitClient
import com.example.apptakeaway.api.SocketManager
import com.example.apptakeaway.model.Order
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.socket.emitter.Emitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderActivity : AppCompatActivity() {
    private val socket = SocketManager.getSocket()
    private lateinit var ordersRecyclerView: RecyclerView
    private lateinit var orderAdapter: OrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        ordersRecyclerView = findViewById(R.id.ordersRecyclerView)
        ordersRecyclerView.layoutManager = LinearLayoutManager(this)

        socket.connect()
        socket.on("orders", updateOrders)

        // Recuperar el userId
        val userId = intent.getIntExtra("userId", -1)

        // Llamada inicial para obtener las órdenes
        loadOrdersForUser(userId)
        setupBackButton()
    }
    private fun displayOrders(orders: List<Order>, userId: Int) {
        // Filtrar las órdenes para que solo se muestren las del usuario específico
        val userOrders = orders.filter { order -> order.userId == userId }

        if (userOrders.isEmpty()) {
            Toast.makeText(this, "No s'han trobat ordres per a aquest usuari", Toast.LENGTH_SHORT)
                .show()
            return
        }

        // Configura el adaptador con las órdenes filtradas
        orderAdapter = OrderAdapter(userOrders)
        findViewById<RecyclerView>(R.id.ordersRecyclerView).adapter = orderAdapter
    }

    private val updateOrders = Emitter.Listener { args ->
        val data = args[0] as String
        val gson = Gson()
        val listType = object : TypeToken<List<Order>>() {}.type
        val updatedOrders: List<Order> = gson.fromJson(data, listType)

        runOnUiThread {
            updatedOrders.forEach { updatedOrder ->
                orderAdapter.updateOrderStatus(updatedOrder.id, updatedOrder.status)
            }
        }
    }

    private fun setupBackButton() {
        findViewById<ImageView>(R.id.backButton).setOnClickListener {
            finish()
        }
    }
    // Llamada en Retrofit
    private fun loadOrdersForUser(userId: Int) {
        if (userId == -1) {
            Toast.makeText(this, "ID d'usuari no vàlid", Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(this@OrderActivity, "No es van trobar comandes", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@OrderActivity, "Error en la resposta: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Order>>, t: Throwable) {
                Log.e("OrderActivity", "Error de xarxa", t)
                Toast.makeText(this@OrderActivity, "Error al caregar la comanda", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()

        // Desconectar los listeners para evitar fugas de memoria
        socket.off("products", updateOrders)

        // Desconectar del socket cuando la actividad se destruye
        socket.disconnect()
    }
}
