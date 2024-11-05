import android.content.Context
import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject
import java.net.URISyntaxException

class SocketManager(context: Context) {

    private val socket: Socket
    private val listeners: MutableMap<String, Emitter.Listener> = mutableMapOf()

    init {
        // Inicializar el socket
        try {
            socket = IO.socket("http://your-server-address:3000") // Cambia esto por tu dirección del servidor
        } catch (e: URISyntaxException) {
            throw RuntimeException(e)
        }
    }

    // Método para conectarse al socket
    fun connect() {
        socket.connect()
        Log.d("SocketManager", "Conectado a Socket.IO")

        // Manejar el evento de conexión
        socket.on(Socket.EVENT_CONNECT) {
            Log.d("SocketManager", "Socket conectado")
        }

        // Manejar el evento de desconexión
        socket.on(Socket.EVENT_DISCONNECT) {
            Log.d("SocketManager", "Socket desconectado")
        }
    }

    // Método para desconectarse del socket
    fun disconnect() {
        socket.disconnect()
        Log.d("SocketManager", "Socket desconectado")
    }

    // Método para escuchar eventos
    fun listen(event: String, listener: (data: JSONObject) -> Unit) {
        // Crea un Emitter.Listener que llama a la función de devolución de llamada
        val listenerWrapper = Emitter.Listener { args ->
            if (args.isNotEmpty() && args[0] is JSONObject) {
                listener(args[0] as JSONObject)
            }
        }

        // Agregar el listener al socket
        socket.on(event, listenerWrapper)
        listeners[event] = listenerWrapper
    }

    // Método para dejar de escuchar eventos
    fun stopListening(event: String) {
        listeners[event]?.let {
            socket.off(event, it)
            listeners.remove(event)
        }
    }

    // Método para enviar datos al servidor
    fun emit(event: String, data: JSONObject) {
        socket.emit(event, data)
    }
}