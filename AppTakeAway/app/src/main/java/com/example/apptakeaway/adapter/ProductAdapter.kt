package com.example.apptakeaway.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apptakeaway.R
import com.example.apptakeaway.model.Product

class ProductAdapter(private val products: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.productName)
        val priceTextView: TextView = view.findViewById(R.id.productPrice)
        val descriptionTextView: TextView = view.findViewById(R.id.productDescription)
        val sizeTextView: TextView = view.findViewById(R.id.productSize)
        val imageView: ImageView = view.findViewById(R.id.productImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.nameTextView.text = product.name
        holder.priceTextView.text = "$${product.price}"
        holder.descriptionTextView.text = product.description
        holder.sizeTextView.text = "Tamaño: ${product.size}"
        Glide.with(holder.imageView.context)
            .load(product.imagePath)
            .into(holder.imageView)
    }

    override fun getItemCount() = products.size
}