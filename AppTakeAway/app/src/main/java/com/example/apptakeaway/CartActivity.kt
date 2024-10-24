package com.example.apptakeaway

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
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

        cartViewModel = ViewModelProvider(applicationContext as ViewModelStoreOwner).get(CartViewModel::class.java)

        val recyclerView : RecyclerView = findViewById(R.id.cartRecyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        cartAdapter = CartAdapter(
            emptyList(),
            { cartItem, newQuantity -> cartViewModel.updateItemQuantity(cartItem, newQuantity) },
            { cartItem -> showRemoveConfirmationDialog(cartItem) },
            { cartItem, isActive -> cartViewModel.updateItemActiveState(cartItem, isActive) }
        )

        recyclerView.adapter = cartAdapter

        totalTextView = findViewById(R.id.totalTextView)

        observeCart()
        setupBackButton()
    }

    private fun observeCart() {
        cartViewModel.cartItems.observe(this) { cartItems ->
            cartAdapter.updateCartItems(cartItems)
            updateTotal()
        }
    }

    private fun updateTotal() {
        val total = cartViewModel.getCartTotal()
        totalTextView.text = "Total : $${String.format("%.2f", total)} "
    }

    private fun showRemoveConfirmationDialog(cartItem : CartItem) {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Confirmació")

        builder.setMessage("Estàs segur que vols eliminar això de la cistella?")

        builder.setPositiveButton("Eliminar") { _, _ ->
            cartViewModel.updateItemQuantity(cartItem, 0)
        }

        builder.setNegativeButton("Cancel·lar", null)

        val dialog = builder.create()

        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(
            ContextCompat.getColor(this,android.R.color.holo_red_light))

    }

    private fun setupBackButton() {

        val backButton : ImageButton = findViewById(R.id.backButton)

        backButton.setOnClickListener {
            finish() // Esto cerrará la actividad actual y volverá a la anterior
        }

    }
}