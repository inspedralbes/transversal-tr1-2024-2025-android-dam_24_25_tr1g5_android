package com.example.apptakeaway

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apptakeaway.adapter.CartAdapter
import com.example.apptakeaway.viewmodel.CartViewModel

class CartActivity : AppCompatActivity() {
    private lateinit var cartViewModel: CartViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart) // Asegúrate de que este layout tenga los IDs correctos

        cartViewModel = ViewModelProvider(this).get(CartViewModel::class.java)

        setupRecyclerView()
        observeCartItems()
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.cartRecyclerView) // Este ID debe coincidir con el definido en activity_cart.xml
        recyclerView.layoutManager = LinearLayoutManager(this)

        cartAdapter = CartAdapter()
        recyclerView.adapter = cartAdapter
    }

    private fun observeCartItems() {
        cartViewModel.cartItems.observe(this) { cartItems ->
            Log.d("CartActivity", "Items en el carrito: ${cartItems.size}")
            cartAdapter.submitList(cartItems) // Asegúrate de que tu adaptador tenga este método
        }
    }
}