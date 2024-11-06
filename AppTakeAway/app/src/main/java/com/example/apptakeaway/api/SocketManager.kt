package com.example.apptakeaway.api

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONArray
import org.json.JSONObject

object SocketManager {
    var socket: Socket? = null

    // Conectar con el servidor Socket.IO
    fun connect() {
        try {
            // URL de tu servidor (ajusta según sea necesario)
            socket = IO.socket("http://192.168.1.16:3000/") // Reemplaza con la URL de tu servidor

            socket?.connect() // Conectar al servidor

            // Escuchar el evento 'products' cuando los productos cambian
            socket?.on("products") { args ->
                val updatedProducts = args[0] as? List<*> ?: emptyList<Any>()
                Log.d("SocketManager", "Productos actualizados: $updatedProducts")

                // Notificar al ViewModel (a través de un LiveData) sobre los productos actualizados
                // Aquí puedes llamar a un método del ViewModel o utilizar un Callback para pasar los datos
                // o almacenar los productos de alguna forma
            }

        } catch (e: Exception) {
            Log.e("SocketManager", "Error al conectar: ${e.message}")
        }
    }

    // Emitir eventos hacia el servidor (por ejemplo, cuando se crea un pedido)
    fun emitEvent(event: String, data: Any) {
        socket?.emit(event, data)
    }

    // Desconectar el socket cuando no se necesita más
    fun disconnect() {
        socket?.disconnect()
    }
}