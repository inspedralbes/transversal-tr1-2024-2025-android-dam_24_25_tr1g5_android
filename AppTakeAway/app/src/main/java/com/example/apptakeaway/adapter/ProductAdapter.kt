package com.example.apptakeaway.adapter // Paquete donde se encuentra el adaptador

/*
 * El `ProductAdapter` es una clase que extiende `ListAdapter` y se encarga de mostrar una lista de productos
 * en un `RecyclerView`. Cada producto se representa mediante un `ProductViewHolder`, que contiene información
 * sobre el producto (imagen, nombre, precio) y un botón para añadir el producto al carrito.
 * La biblioteca Glide se utiliza para cargar imágenes de los productos de manera eficiente.
 * Además, se implementa un callback para manejar la acción de añadir un producto al carrito.
 */

import android.view.LayoutInflater // Para inflar los layouts de vista
import android.view.View // Clase base para la visualización de componentes
import android.view.ViewGroup // Contenedor de vistas
import android.widget.Button // Clase para crear botones
import android.widget.ImageView // Clase para mostrar imágenes
import android.widget.TextView // Clase para mostrar texto
import androidx.recyclerview.widget.DiffUtil // Herramienta para calcular diferencias entre listas
import androidx.recyclerview.widget.ListAdapter // Adaptador para listas que usan DiffUtil
import androidx.recyclerview.widget.RecyclerView // Componente para mostrar listas de datos
import com.bumptech.glide.Glide // Biblioteca para cargar imágenes
import com.example.apptakeaway.R // Recursos de la aplicación
import com.example.apptakeaway.model.Product // Modelo de los productos

// Clase que representa el adaptador de productos
class ProductAdapter(
    private val onAddToCart: (Product) -> Unit // Callback para añadir al carrito
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) { // Hereda de ListAdapter

    // Clase interna que actúa como ViewHolder para un ítem del producto
    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Referencias a las vistas dentro del layout del ítem
        val imageView: ImageView = view.findViewById(R.id.productImage) // Imagen del producto
        val nameTextView: TextView = view.findViewById(R.id.productName) // Nombre del producto
        val priceTextView: TextView = view.findViewById(R.id.productPrice) // Precio del producto
        val addButton: Button = view.findViewById(R.id.addButton) // Botón para añadir al carrito
    }

    // Método que crea el ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        // Inflar el layout de cada ítem del producto
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_item, parent, false) // Layout específico para un ítem del producto
        return ProductViewHolder(view) // Retorna el ViewHolder creado
    }

    // Método que vincula los datos del producto al ViewHolder
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        // Obtener el producto correspondiente a la posición
        val product = getItem(position)
        holder.nameTextView.text = product.name // Asignar el nombre del producto
        holder.priceTextView.text = "$${product.price}" // Asignar el precio del producto

        // Cargar la imagen del producto usando Glide
        Glide.with(holder.imageView.context)
            .load("http://name.tr1-g5.dam.inspedralbes.cat:21787/" + product.imagePath) // Ruta de la imagen
            .error(android.R.drawable.ic_menu_gallery) // Placeholder en caso de error
            .into(holder.imageView) // Cargar la imagen en el ImageView

        // Configurar el listener del botón para añadir al carrito
        holder.addButton.setOnClickListener {
            onAddToCart(product) // Llama al callback para añadir el producto al carrito
        }
    }
}

// Implementación de DiffUtil para optimizar las actualizaciones de la lista
class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
    // Comprobar si dos ítems son el mismo
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id // Asumiendo que 'id' es único para cada producto
    }

    // Comprobar si el contenido de dos ítems es el mismo
    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem // Compara el contenido completo del objeto
    }
}
