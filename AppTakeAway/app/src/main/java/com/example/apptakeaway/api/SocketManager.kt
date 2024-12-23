package com.example.apptakeaway.api

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

object SocketManager {
    private const val SOCKET_URL = "http://name.tr1-g5.dam.inspedralbes.cat:21787/" // Cambia esto a la URL de tu servidor
    private lateinit var socket: Socket

    init {
        try {
            val options = IO.Options().apply {
                reconnection = true // Reconexión automática
            }
            socket = IO.socket(SOCKET_URL, options)
        } catch (e: URISyntaxException) {
            Log.e("SocketManager", "Error al conectar Socket.IO: ${e.message}")
        }
    }

    fun getSocket(): Socket {
        return socket
    }
}