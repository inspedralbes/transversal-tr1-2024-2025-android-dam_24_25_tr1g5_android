package com.example.apptakeaway.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apptakeaway.R
import com.example.apptakeaway.model.Order
import com.example.apptakeaway.model.Product

class OrderProductAdapter(private val orderProducts: List<Product>) : RecyclerView.Adapter<OrderProductAdapter.OrderProductViewHolder>() {

    class OrderProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderProductImageView: ImageView = itemView.findViewById(R.id.orderProductImageView)
        val orderProductNameTextView: TextView = itemView.findViewById(R.id.orderProductNameTextView)
        val orderProductSizeTextView: TextView = itemView.findViewById(R.id.orderProductSizeTextView)
        val orderProductPriceTextView: TextView = itemView.findViewById(R.id.orderProductPriceTextView)
        val orderProductStockTextView: TextView = itemView.findViewById(R.id.orderProductStockTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_product_item, parent, false)
        return OrderProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderProductViewHolder, position: Int) {
        val orderProduct = orderProducts[position]

        // Asignar nombre, tamaño, precio y cantidad del producto en la orden
        holder.orderProductNameTextView.text = orderProduct.name
        holder.orderProductSizeTextView.text = "Tamaño: ${orderProduct.size}"
        holder.orderProductPriceTextView.text = "Precio: $${orderProduct.price}"
        holder.orderProductStockTextView.text = "Cantidad disponible: ${orderProduct.stock}"

        // Cargar la imagen con Glide
        Glide.with(holder.itemView.context)
            .load(orderProduct.imagePath) // URL o ruta de la imagen del producto
            .placeholder(R.drawable.ic_placeholder) // Imagen de placeholder
            .error(R.drawable.logo_name) // Imagen de error si falla la carga
            .into(holder.orderProductImageView)
    }

    override fun getItemCount(): Int = orderProducts.size
}
