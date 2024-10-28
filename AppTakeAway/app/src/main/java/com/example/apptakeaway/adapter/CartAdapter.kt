package com.example.apptakeaway.adapter // Paquete donde se encuentra el adaptador

/*
 * El `CartAdapter` es una clase que extiende `ListAdapter` y se encarga de mostrar los elementos de un carrito de compras
 * dentro de un `RecyclerView`. Cada elemento del carrito se representa mediante un `ViewHolder` que contiene información
 * sobre el producto (imagen, nombre, precio) y permite al usuario incrementar o decrementar la cantidad de productos
 * seleccionados. El adaptador también incluye la funcionalidad para seleccionar artículos utilizando un `CheckBox`.
 * Además, se utiliza la biblioteca Glide para cargar imágenes de forma eficiente.
 */

import android.view.LayoutInflater // Para inflar los layouts de vista
import android.view.View // Clase base para la visualización de componentes
import android.view.ViewGroup // Contenedor de vistas
import android.widget.Button // Clase para crear botones
import android.widget.CheckBox // Clase para crear casillas de verificación
import android.widget.ImageView // Clase para mostrar imágenes
import android.widget.TextView // Clase para mostrar texto
import androidx.recyclerview.widget.DiffUtil // Herramienta para calcular diferencias entre listas
import androidx.recyclerview.widget.ListAdapter // Adaptador para listas que usan DiffUtil
import androidx.recyclerview.widget.RecyclerView // Componente para mostrar listas de datos
import com.bumptech.glide.Glide // Biblioteca para cargar imágenes
import com.bumptech.glide.load.engine.DiskCacheStrategy // Estrategia de caché de imágenes
import com.bumptech.glide.request.RequestOptions // Opciones para las peticiones de imagen
import com.example.apptakeaway.R // Recursos de la aplicación
import com.example.apptakeaway.model.CartItem // Modelo de los elementos del carrito

// Clase que representa el adaptador del carrito
class CartAdapter(
    private val onQuantityChanged: (CartItem, Int) -> Unit // Función para manejar cambios de cantidad
) : ListAdapter<CartItem, CartAdapter.ViewHolder>(CartDiffCallback()) { // Hereda de ListAdapter

    // Método que crea el ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflar el layout de cada ítem del carrito
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_item, parent, false) // Layout específico para un ítem del carrito
        return ViewHolder(view) // Retorna el ViewHolder creado
    }

    // Método que vincula los datos del ítem al ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Obtener el ítem correspondiente a la posición
        val item = getItem(position)
        holder.bind(item) // Vincula el ítem al ViewHolder
    }

    // Clase interna que actúa como ViewHolder para un ítem del carrito
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Referencias a las vistas dentro del layout del ítem
        private val productImage: ImageView = view.findViewById(R.id.productImage) // Imagen del producto
        private val productName: TextView = view.findViewById(R.id.productName) // Nombre del producto
        private val productPrice: TextView = view.findViewById(R.id.productPrice) // Precio del producto
        private val quantity: TextView = view.findViewById(R.id.quantityTextView) // Texto para la cantidad
        private val decrementButton: Button = view.findViewById(R.id.decrementButton) // Botón para decrementar
        private val incrementButton: Button = view.findViewById(R.id.incrementButton) // Botón para incrementar
        private val checkBox: CheckBox = view.findViewById(R.id.checkBox) // Casilla de verificación

        // Inicialización de los listeners para los botones y el CheckBox
        init {
            decrementButton.setOnClickListener {
                // Al hacer clic en el botón de decrementar
                val item = getItem(adapterPosition) // Obtener el ítem actual
                if (item.quantity > 1) {
                    // Si la cantidad es mayor a 1, decrementar
                    onQuantityChanged(item, item.quantity - 1)
                } else {
                    // Si la cantidad es 0 o 1, remover del carrito
                    onQuantityChanged(item, 0)
                }
            }

            incrementButton.setOnClickListener {
                // Al hacer clic en el botón de incrementar
                val item = getItem(adapterPosition) // Obtener el ítem actual
                onQuantityChanged(item, item.quantity + 1) // Incrementar la cantidad
            }

            // Configuración del CheckBox, si se requiere lógica adicional
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                // Al cambiar el estado del CheckBox
                val item = getItem(adapterPosition) // Obtener el ítem actual
                item.isSelected = isChecked // Actualizar el estado del ítem
            }
        }

        // Método para vincular el ítem a las vistas del ViewHolder
        fun bind(item: CartItem) {
            productName.text = item.product.name // Asignar el nombre del producto
            productPrice.text = String.format("$%.2f", item.product.price.toDouble()) // Formatear el precio
            updateQuantityDisplay(item.quantity) // Actualizar la visualización de la cantidad

            // Cargar la imagen del producto usando Glide
            Glide.with(itemView.context)
                .load("http://name.tr1-g5.dam.inspedralbes.cat:21787/" + item.product.imagePath) // Ruta de la imagen
                .apply(RequestOptions()
                    .placeholder(android.R.drawable.ic_menu_gallery) // Imagen de carga
                    .error(android.R.drawable.ic_menu_gallery) // Imagen en caso de error
                    .diskCacheStrategy(DiskCacheStrategy.ALL)) // Estrategia de caché
                .into(productImage) // Cargar la imagen en el ImageView

            // Configurar el estado del CheckBox
            checkBox.isChecked = item.isSelected // Marcar el CheckBox según el estado del ítem
        }

        // Método para actualizar la visualización de la cantidad
        private fun updateQuantityDisplay(quantity: Int) {
            this.quantity.text = quantity.toString() // Actualizar el texto de cantidad
        }
    }

    // Clase interna para manejar diferencias entre ítems del carrito
    private class CartDiffCallback : DiffUtil.ItemCallback<CartItem>() {
        // Comprobar si dos ítems son el mismo
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.product.id == newItem.product.id // Comparar por ID del producto
        }

        // Comprobar si el contenido de dos ítems es el mismo
        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            // Comparar todos los campos relevantes
            return oldItem.product == newItem.product && oldItem.quantity == newItem.quantity
        }
    }
}
