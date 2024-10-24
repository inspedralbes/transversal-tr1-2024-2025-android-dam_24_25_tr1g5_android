package com.example.apptakeaway.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apptakeaway.R
import com.example.apptakeaway.model.Product

class ProductAdapter(
    private val onAddToCart: (Product) -> Unit // Callback para añadir al carrito
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.productImage)
        val nameTextView: TextView = view.findViewById(R.id.productName)
        val priceTextView: TextView = view.findViewById(R.id.productPrice)
        val addButton: Button = view.findViewById(R.id.addButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.nameTextView.text = product.name
        holder.priceTextView.text = "$${product.price}"

        Glide.with(holder.imageView.context)
            .load(product.imagePath)
            .error(android.R.drawable.ic_menu_gallery) // Placeholder en caso de error
            .into(holder.imageView)

        holder.addButton.setOnClickListener {
            onAddToCart(product) // Llama al callback para añadir al carrito
        }
    }
}

// Implementación de DiffUtil para optimizar las actualizaciones de la lista
class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id // Asumiendo que 'id' es único para cada producto
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem // Compara el contenido completo del objeto
    }
}