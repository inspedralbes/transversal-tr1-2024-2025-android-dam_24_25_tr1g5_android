package com.example.apptakeaway

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apptakeaway.adapter.CartAdapter
import com.example.apptakeaway.model.CartItem
import com.example.apptakeaway.viewmodel.CartViewModel

class CartActivity : AppCompatActivity() {
    private lateinit var cartViewModel: CartViewModel
    private lateinit var cartAdapter: CartAdapter
    private lateinit var totalTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        cartViewModel = (application as AppTakeAwayApplication).cartViewModel

        setupRecyclerView()
        setupBackButton()
        observeCartItems()
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.cartRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        cartAdapter = CartAdapter { item, newQuantity ->
            if (newQuantity > 0) {
                cartViewModel.updateItemQuantity(item, newQuantity)
            } else {
                showRemoveItemDialog(item)
            }
        }
        recyclerView.adapter = cartAdapter
    }

    private fun setupBackButton() {
        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            finish()
        }
    }

    private fun observeCartItems() {
        totalTextView = findViewById(R.id.totalTextView)
        cartViewModel.cartItems.observe(this) { cartItems ->
            Log.d("CartActivity", "Actualizando lista de items: ${cartItems.size}")
            cartItems.forEach { item ->
                Log.d("CartActivity", "Item: ${item.product.name}, Cantidad: ${item.quantity}")
            }
            cartAdapter.submitList(cartItems.toList())
            updateTotal()
        }
    }

    private fun updateTotal() {
        val total = cartViewModel.getCartTotal()
        totalTextView.text = String.format("Total: $%.2f", total)
    }

    private fun showRemoveItemDialog(item: CartItem) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar producto")
            .setMessage("¿Estás seguro de que quieres eliminar ${item.product.name} del carrito?")
            .setPositiveButton("Sí") { _, _ ->
                cartViewModel.removeFromCart(item)
            }
            .setNegativeButton("No", null)
            .show()
    }
}