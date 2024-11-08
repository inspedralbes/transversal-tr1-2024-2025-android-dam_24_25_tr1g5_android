package com.example.apptakeaway.adapter // Paquete donde se encuentra el adaptador

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apptakeaway.R
import com.example.apptakeaway.model.Product

/**
 * Adaptador para mostrar los productos de una orden.
 * Cada ítem contiene información sobre un producto asociado a la orden.
 */
class OrderProductAdapter(private val products: List<Product>) : RecyclerView.Adapter<OrderProductAdapter.OrderProductViewHolder>() {

    class OrderProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productNameTextView: TextView = itemView.findViewById(R.id.productNameTextView)
        val productDescriptionTextView: TextView = itemView.findViewById(R.id.productDescriptionTextView)
        val productPriceTextView: TextView = itemView.findViewById(R.id.productPriceTextView)
        val productColorsTextView: TextView = itemView.findViewById(R.id.productColorsTextView)
        val productStockTextView: TextView = itemView.findViewById(R.id.productStockTextView)
        val productImageView: ImageView = itemView.findViewById(R.id.productImageView) // Aquí se agregará la imagen
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_product_item, parent, false)
        return OrderProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderProductViewHolder, position: Int) {
        val product = products[position]

        holder.productNameTextView.text = product.name
        holder.productDescriptionTextView.text = product.description
        holder.productPriceTextView.text = "Precio: \$${product.price}"
        holder.productColorsTextView.text = "Colores: ${product.colors}"
        holder.productStockTextView.text = "Stock: ${product.stock}"

        // Usando Glide para cargar la imagen del producto
        Glide.with(holder.itemView.context)
            .load("http://name.tr1-g5.dam.inspedralbes.cat:21787/" + product.imagePath)  // Cargar la imagen desde la URL
            .placeholder(R.drawable.ic_placeholder)  // Imagen que aparece mientras se carga la imagen real
            .error(R.drawable.user_icon)  // Imagen que aparece si ocurre un error al cargar
            .into(holder.productImageView)
    }

    override fun getItemCount(): Int = products.size
}

