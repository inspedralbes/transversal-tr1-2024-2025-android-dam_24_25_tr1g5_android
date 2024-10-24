package com.example.apptakeaway.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apptakeaway.R
import com.example.apptakeaway.model.CartItem

class CartAdapter(
    private var cartItems: List<CartItem>,
    private val onItemChanged: (CartItem, Int) -> Unit,
    private val onItemRemove: (CartItem) -> Unit,
    private val onItemCheckedChangeListener: (CartItem, Boolean) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productImage: ImageView = view.findViewById(R.id.productImage)
        val productName: TextView = view.findViewById(R.id.productName)
        val productPrice: TextView = view.findViewById(R.id.productPrice)
        val quantityTextView: TextView = view.findViewById(R.id.quantityTextView)
        val decrementButton: Button = view.findViewById(R.id.decrementButton)
        val incrementButton: Button = view.findViewById(R.id.incrementButton)
        val activeCheckBox: CheckBox = view.findViewById(R.id.activeCheckBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]

        holder.productName.text = cartItem.product.name
        holder.productPrice.text = "$${cartItem.product.price}"
        holder.quantityTextView.text = cartItem.quantity.toString()

        Glide.with(holder.productImage.context)
            .load(cartItem.product.imagePath)
            .into(holder.productImage)

        holder.activeCheckBox.isChecked = cartItem.isActive

        holder.decrementButton.setOnClickListener {
            if (cartItem.quantity > 1) {
                onItemChanged(cartItem, cartItem.quantity - 1)
            } else {
                onItemRemove(cartItem)
            }
        }

        holder.incrementButton.setOnClickListener {
            onItemChanged(cartItem, cartItem.quantity + 1)
        }

        holder.activeCheckBox.setOnCheckedChangeListener { _, isChecked ->
            onItemCheckedChangeListener(cartItem, isChecked)
        }
    }

    override fun getItemCount() = cartItems.size

    fun updateCartItems(newCartItems: List<CartItem>) {
        cartItems = newCartItems
        notifyDataSetChanged()
    }
}