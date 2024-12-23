package com.example.apptakeaway

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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
    private lateinit var payButton: Button

    private var userId: Int = -1 // Variable para almacenar el userId
    private var isLoggedIn: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Inicializa los ViewModels
        cartViewModel = (application as AppTakeAwayApplication).cartViewModel

        // Obtiene el userId del Intent
        userId = intent.getIntExtra("userId", -1)
        isLoggedIn = intent.getBooleanExtra("isLoggedIn", false)

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
        findViewById<Button>(R.id.payButton).setOnClickListener {
            cartViewModel.addItemsToPayItems() // Llama a la función para actualizar `payItems`

            // Observa los cambios en `payItems` antes de navegar
            cartViewModel.payItems.observe(this) { payItems ->
                if (payItems.any { it.isSelected }) { // Asegura que haya elementos seleccionados
                    val selectedPayItems = payItems.filter { it.isSelected }

                    // Crear el Intent para iniciar PayActivity
                    val intent = Intent(this, PayActivity::class.java).apply {
                        putExtra("payItems", ArrayList(selectedPayItems))
                        putExtra("userId", userId) // Pasa el userId a PayActivity
                        putExtra("isLoggedIn", isLoggedIn)
                    }

                    // Iniciar PayActivity
                    startActivity(intent)

                    // Deja de observar después de la navegación para evitar múltiples lanzamientos
                    cartViewModel.payItems.removeObservers(this)
                }
            }
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
            payButton.text = if (hasSelectedItems) "PAGA" else "PAGA"
        }
    }

    private fun updateTotal() {
        val total = cartViewModel.getCartTotal()
        totalTextView.text = String.format("Total: %.2f€", total)
    }

    private fun showRemoveItemDialog(item: CartItem) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar producte")
            .setMessage("Estàs segur que vols eliminar ${item.product.name} del cistella?")
            .setPositiveButton("Sí") { _, _ ->
                cartViewModel.removeFromCart(item)
            }
            .setNegativeButton("No", null)
            .show()
    }
}
