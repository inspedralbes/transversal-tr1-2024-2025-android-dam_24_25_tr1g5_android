package com.example.apptakeaway.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.apptakeaway.R
import com.example.apptakeaway.model.CartItem

class CartAdapter(
    private val onQuantityChanged: (CartItem, Int) -> Unit
) : ListAdapter<CartItem, CartAdapter.ViewHolder>(CartDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val productName: TextView = view.findViewById(R.id.productName)
        private val productPrice: TextView = view.findViewById(R.id.productPrice)
        private val quantity: TextView = view.findViewById(R.id.quantityTextView)
        private val decrementButton: Button = view.findViewById(R.id.decrementButton)
        private val incrementButton: Button = view.findViewById(R.id.incrementButton)

        fun bind(item: CartItem) {
            productName.text = item.product.name
            productPrice.text = String.format("$%.2f", item.product.price.toDouble())
            updateQuantityDisplay(item.quantity)

            decrementButton.setOnClickListener {
                if (item.quantity > 1) {
                    onQuantityChanged(item, item.quantity - 1)
                } else {
                    onQuantityChanged(item, 0)
                }
            }

            incrementButton.setOnClickListener {
                onQuantityChanged(item, item.quantity + 1)
            }
        }

        private fun updateQuantityDisplay(quantity: Int) {
            this.quantity.text = quantity.toString()
        }
    }

    private class CartDiffCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.product.id == newItem.product.id
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.quantity == newItem.quantity && oldItem.product == newItem.product
        }
    }
}