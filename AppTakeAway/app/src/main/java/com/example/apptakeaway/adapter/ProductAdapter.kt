package com.example.apptakeaway.adapter

import android.util.Log
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
import io.socket.emitter.Emitter
import org.json.JSONArray

class ProductAdapter(
    private val onAddToCart: (Product) -> Unit
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.productImage)
        val nameTextView: TextView = view.findViewById(R.id.productName)
        val priceTextView: TextView = view.findViewById(R.id.productPrice)
        val addButton: Button = view.findViewById(R.id.addButton)
    }

    // Listener para recibir productos actualizados
    val onProducts = Emitter.Listener { args ->
        val data = args[0] as JSONArray
        val updatedProducts = mutableListOf<Product>()

        for (i in 0 until data.length()) {
            val jsonProduct = data.getJSONObject(i)
            val product = Product(
                id = jsonProduct.getInt("id"),
                categoryId = jsonProduct.getInt("categoryId"),
                name = jsonProduct.getString("name"),
                description = jsonProduct.getString("description"),
                size = jsonProduct.getString("size"),
                price = jsonProduct.getDouble("price"),
                imagePath = jsonProduct.getString("imagePath"),
                colors = jsonProduct.getString("colors"),
                stock = jsonProduct.getInt("stock"),
                activated = jsonProduct.getInt("activated")
            )
            updatedProducts.add(product)
        }

        submitList(updatedProducts)
        Log.d("SocketEvent", "Productes actualitzats: $updatedProducts")
    }

    fun getOnProductsListener(): Emitter.Listener = onProducts

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.nameTextView.text = product.name
        holder.priceTextView.text = "$${product.price}"

        Glide.with(holder.imageView.context)
            .load("http://name.tr1-g5.dam.inspedralbes.cat:21787/" + product.imagePath)
            .error(android.R.drawable.ic_menu_gallery)
            .into(holder.imageView)

        holder.addButton.setOnClickListener {
            onAddToCart(product)
        }
    }
}

class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}
