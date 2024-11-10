// PayAdapter.kt
package com.example.apptakeaway.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apptakeaway.R
import com.example.apptakeaway.model.CartItem
import com.bumptech.glide.Glide // Asegúrate de agregar Glide a tus dependencias para cargar imágenes

class PayAdapter(private val payItems: List<CartItem>) : RecyclerView.Adapter<PayAdapter.PayViewHolder>() {

    class PayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImageView: ImageView = itemView.findViewById(R.id.productImageView)
        val productTextQuantity: TextView = itemView.findViewById(R.id.quantityTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pay_item, parent, false)
        return PayViewHolder(view)
    }

    override fun onBindViewHolder(holder: PayViewHolder, position: Int) {
        val cartItem = payItems[position]

        // Cargar la imagen del producto usando Glide
        Glide.with(holder.itemView.context)
            .load("http://name.tr1-g5.dam.inspedralbes.cat:21787/" + cartItem.product.imagePath)
            .placeholder(R.drawable.ic_placeholder)
            .into(holder.productImageView)

        // Asegúrate de que 'quantity' exista en 'cartItem' y asígnalo al TextView
        holder.productTextQuantity.text = "Quantitat: ${cartItem.quantity}"
    }

    override fun getItemCount(): Int = payItems.size
}
