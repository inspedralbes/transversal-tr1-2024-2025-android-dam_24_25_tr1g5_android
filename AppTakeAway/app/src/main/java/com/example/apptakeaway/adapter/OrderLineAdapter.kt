package com.example.apptakeaway.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apptakeaway.R
import com.example.apptakeaway.model.OrderLine


class OrderLineAdapter(private val orderLines: List<OrderLine>) : RecyclerView.Adapter<OrderLineAdapter.OrderLineViewHolder>() {

    inner class OrderLineViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productNameTextView: TextView = view.findViewById(R.id.productNameTextView)
        val productPrecioTextView: TextView = view.findViewById(R.id.productPrecioTextView)
        val productImgImageView: ImageView = view.findViewById(R.id.productImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderLineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_line_item, parent, false)
        return OrderLineViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderLineViewHolder, position: Int) {
        val orderLine = orderLines[position]
        holder.productNameTextView.text = orderLine.productName
        holder.productPrecioTextView.text = orderLine.productPrice
        Glide.with(holder.productImgImageView.context)
            .load("http://name.tr1-g5.dam.inspedralbes.cat:21787/" + orderLine.productImagePath) // Ruta de la imagen
            .error(android.R.drawable.ic_menu_gallery) // Placeholder en caso de error
            .into(holder.productImgImageView)
    }

    override fun getItemCount() = orderLines.size
}