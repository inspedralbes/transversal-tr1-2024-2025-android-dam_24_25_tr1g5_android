package com.example.apptakeaway // Paquete donde se encuentra la actividad del carrito

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apptakeaway.adapter.CartAdapter
import com.example.apptakeaway.model.CartItem
import com.example.apptakeaway.viewmodel.CartViewModel
import com.example.apptakeaway.viewmodel.PayViewModel // Importa el PayViewModel

class CartActivity : AppCompatActivity() {
    private lateinit var cartViewModel: CartViewModel
    private lateinit var payViewModel: PayViewModel // Nueva referencia al PayViewModel
    private lateinit var cartAdapter: CartAdapter
    private lateinit var totalTextView: TextView
    private lateinit var payButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Inicializa los ViewModels
        cartViewModel = (application as AppTakeAwayApplication).cartViewModel
        payViewModel = ViewModelProvider(this).get(PayViewModel::class.java) // Inicializa PayViewModel
        payButton = findViewById(R.id.payButton)

        setupRecyclerView()
        setupBackButton()
        setupPayButton()
        observeCartItems()
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.cartRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        cartAdapter = CartAdapter(
            onQuantityChanged = { item, newQuantity ->
                if (newQuantity > 0) {
                    cartViewModel.updateItemQuantity(item, newQuantity)
                } else {
                    showRemoveItemDialog(item)
                }
            },
            onSelectionChanged = { cartViewModel.updateItemSelection(it) }
        )

        recyclerView.adapter = cartAdapter
    }

    private fun setupBackButton() {
        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            finish()
        }
    }

    private fun setupPayButton() {
        payButton.setOnClickListener {
            // Transferir los ítems seleccionados al PayViewModel
            payViewModel.transferSelectedItems(cartViewModel.cartItems.value ?: emptyList())

            // Inicia PayActivity
            val intent = Intent(this, PayActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeCartItems() {
        totalTextView = findViewById(R.id.totalTextView)

        cartViewModel.cartItems.observe(this) { cartItems ->
            cartAdapter.submitList(cartItems.toList())
            cartAdapter.notifyDataSetChanged()
            updateTotal()

            val hasSelectedItems = cartItems.any { it.isSelected }
            payButton.isEnabled = hasSelectedItems
            payButton.alpha = if (hasSelectedItems) 1.0f else 0.5f
            payButton.text = if (hasSelectedItems) "PAY" else "PAY"
        }
    }

    private fun updateTotal() {
        val total = cartViewModel.getCartTotal()
        totalTextView.text = String.format("Total: $%.2f", total)
    }

    private fun showRemoveItemDialog(item: CartItem) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar producto")
            .setMessage("¿Estás seguro de que deseas eliminar ${item.product.name} del carrito?")
            .setPositiveButton("Sí") { _, _ ->
                cartViewModel.removeFromCart(item)
            }
            .setNegativeButton("No", null)
            .show()
    }
}
