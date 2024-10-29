package com.example.apptakeaway // Paquete donde se encuentra la actividad del carrito

/*
 * La clase `CartActivity` es responsable de mostrar el carrito de compras al usuario.
 * Extiende `AppCompatActivity` para gestionar la interfaz de usuario y las interacciones del
 * usuario relacionadas con los elementos en el carrito. Aquí se inicializa el RecyclerView para
 * mostrar los elementos del carrito, se configura un adaptador para manejar la lista de productos,
 * y se implementan funcionalidades como la actualización de cantidades y la eliminación de productos.
 */

import android.content.Intent
import android.os.Bundle // Importa la clase Bundle para pasar datos entre actividades
import android.util.Log // Importa la clase Log para registrar mensajes de depuración
import android.widget.ImageButton // Importa la clase ImageButton para botones de imagen
import android.widget.TextView // Importa la clase TextView para mostrar texto
import androidx.appcompat.app.AlertDialog // Importa la clase AlertDialog para diálogos de alerta
import androidx.appcompat.app.AppCompatActivity // Clase base para actividades que utilizan la biblioteca de soporte
import androidx.recyclerview.widget.LinearLayoutManager // Importa el gestor de diseño de lista vertical
import androidx.recyclerview.widget.RecyclerView // Importa la clase RecyclerView para listas eficientes
import com.example.apptakeaway.adapter.CartAdapter // Importa el adaptador del carrito
import com.example.apptakeaway.model.CartItem // Importa el modelo de CartItem
import com.example.apptakeaway.viewmodel.CartViewModel // Importa el ViewModel del carrito

// Clase que representa la actividad del carrito de compras
class CartActivity : AppCompatActivity() {
    private lateinit var cartViewModel: CartViewModel // ViewModel que gestiona los datos del carrito
    private lateinit var cartAdapter: CartAdapter // Adaptador para mostrar los elementos del carrito
    private lateinit var totalTextView: TextView // TextView para mostrar el total del carrito

    // Método que se llama al crear la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // Llama al método de la clase base
        setContentView(R.layout.activity_cart) // Establece el layout de la actividad

        // Inicializa el ViewModel del carrito
        cartViewModel = (application as AppTakeAwayApplication).cartViewModel

        setupRecyclerView() // Configura el RecyclerView para mostrar los elementos del carrito
        setupBackButton() // Configura el botón de retroceso
        setupPayButton()
        observeCartItems() // Observa los elementos del carrito para actualizaciones
    }

    // Método para configurar el RecyclerView
    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.cartRecyclerView) // Encuentra el RecyclerView en el layout
        recyclerView.layoutManager = LinearLayoutManager(this) // Establece el gestor de diseño para una lista vertical

        // Inicializa el adaptador del carrito, con manejadores para cambios en cantidad y selección
        cartAdapter = CartAdapter(
            onQuantityChanged = { item, newQuantity ->
                if (newQuantity > 0) {
                    cartViewModel.updateItemQuantity(item, newQuantity) // Actualiza la cantidad del item
                } else {
                    showRemoveItemDialog(item) // Muestra un diálogo de confirmación para eliminar el item
                }
            },
            onSelectionChanged = { cartViewModel.updateItemSelection(it) } // Llama a un método para actualizar la selección
        )

        recyclerView.adapter = cartAdapter // Establece el adaptador en el RecyclerView
    }

    // Método para configurar el botón de retroceso
    private fun setupBackButton() {
        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            finish() // Cierra la actividad actual y vuelve a la anterior
        }
    }
    private fun setupPayButton() {
        findViewById<ImageButton>(R.id.payButton).setOnClickListener {
            // Inicia PayActivity
            val intent = Intent(this, PayActivity::class.java)
            startActivity(intent) // Llama a startActivity con el Intent creado
        }
    }

    // Método para observar los elementos del carrito
    private fun observeCartItems() {
        totalTextView = findViewById(R.id.totalTextView) // Encuentra el TextView para mostrar el total
        cartViewModel.cartItems.observe(this) { cartItems -> // Observa los cambios en los elementos del carrito
            cartAdapter.submitList(cartItems.toList()) // Actualiza la lista del adaptador con los nuevos items
            cartAdapter.notifyDataSetChanged() // Fuerza el refresco de la lista
            updateTotal() // Actualiza el total del carrito
        }
    }

    // Método para actualizar el total del carrito
    private fun updateTotal() {
        val total = cartViewModel.getCartTotal() // Obtiene el total de los items en el carrito
        totalTextView.text = String.format("Total: $%.2f", total) // Muestra el total en el TextView
    }

    // Método para mostrar un diálogo de confirmación para eliminar un item
    private fun showRemoveItemDialog(item: CartItem) {
        AlertDialog.Builder(this) // Crea un constructor para un diálogo de alerta
            .setTitle("Eliminar producto") // Título del diálogo
            .setMessage("¿Estás seguro de que deseas eliminar ${item.product.name} del carrito?") // Mensaje
            .setPositiveButton("Sí") { _, _ ->
                cartViewModel.removeFromCart(item) // Elimina el item del carrito si se confirma
            }
            .setNegativeButton("No", null) // Botón para cancelar la acción
            .show() // Muestra el diálogo
    }
}
