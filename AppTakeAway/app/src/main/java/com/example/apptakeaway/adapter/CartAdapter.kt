package com.example.apptakeaway.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.apptakeaway.R
import com.example.apptakeaway.model.CartItem

class CartAdapter : ListAdapter<CartItem, CartAdapter.CartViewHolder>(CartDiffCallback()) {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.productName)
        val quantityTextView: TextView = view.findViewById(R.id.quantityTextView) // Asegúrate de que este ID coincida
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = getItem(position)

        holder.nameTextView.text = cartItem.product.name
        holder.quantityTextView.text = "Cantidad: ${cartItem.quantity}"

        // Aquí puedes agregar lógica para manejar los botones de incrementar y decrementar si es necesario.
    }
}

// Implementación de DiffUtil para optimizar las actualizaciones de la lista
class CartDiffCallback : DiffUtil.ItemCallback<CartItem>() {
    override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
        return oldItem.product.id == newItem.product.id // Asumiendo que 'id' es único para cada producto
    }

    override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
        return oldItem == newItem // Compara el contenido completo del objeto
    }
}