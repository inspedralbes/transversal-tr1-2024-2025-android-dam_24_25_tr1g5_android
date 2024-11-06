package com.example.apptakeaway.api

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

object SocketManager {
    private const val SOCKET_URL = "http://ip:3000/" // Cambia esto a la URL de tu servidor
    private var socket: Socket? = null

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

    fun getSocket(): Socket? {
        return socket
    }

    fun connect() {
        socket?.connect()
    }

    fun disconnect() {
        socket?.disconnect()
    }
}