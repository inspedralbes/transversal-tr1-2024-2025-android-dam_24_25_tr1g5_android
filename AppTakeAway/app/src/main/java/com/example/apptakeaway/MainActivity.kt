package com.example.apptakeaway

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apptakeaway.adapter.ProductAdapter
import com.example.apptakeaway.viewmodel.CartViewModel
import com.example.apptakeaway.viewmodel.ProductViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var productViewModel: ProductViewModel
    private lateinit var cartViewModel: CartViewModel
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        productViewModel = ProductViewModel()
        cartViewModel = (application as AppTakeAwayApplication).cartViewModel

        setupRecyclerView()
        setupSearchView()
        setupCartButton()
        observeProducts()
        observeCart()

        progressBar = findViewById(R.id.progressBar)

        loadProducts()
    }

    private fun loadProducts() {
        progressBar.visibility = View.VISIBLE
        productViewModel.loadProducts()
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)
        val layoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = layoutManager

        productAdapter = ProductAdapter { product ->
            cartViewModel.addToCart(product)
            Log.d("MainActivity", "Producto añadido al carrito: ${product.name}")
            Toast.makeText(this, "${product.name} añadido al carrito", Toast.LENGTH_SHORT).show()
        }

        recyclerView.adapter = productAdapter
    }

    private fun setupSearchView() {
        val searchView = findViewById<SearchView>(R.id.searchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                productViewModel.filterProducts(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                productViewModel.filterProducts(newText)
                return true
            }
        })
    }

    private fun observeProducts() {
        productViewModel.products.observe(this) { products ->
            progressBar.visibility = View.GONE

            if (products.isNotEmpty()) {
                productAdapter.submitList(products)
                Log.d("MainActivity", "Productos cargados: ${products.size}")
            } else {
                Toast.makeText(this, "No se encontraron productos", Toast.LENGTH_SHORT).show()
                Log.d("MainActivity", "No se encontraron productos")
            }
        }
    }

    private fun setupCartButton() {
        val cartButton = findViewById<ImageButton>(R.id.cartButton)

        cartButton.setOnClickListener {
            Log.d("MainActivity", "Navegando a CartActivity")
            startActivity(Intent(this, CartActivity::class.java))
        }
    }

    private fun observeCart() {
        cartViewModel.cartItems.observe(this) { cartItems ->
            updateCartBadge(cartItems.sumOf { it.quantity })
            Log.d("MainActivity", "Carrito actualizado: ${cartItems.size} items")
        }
    }

    private fun updateCartBadge(itemCount: Int) {
        val cartButton = findViewById<ImageButton>(R.id.cartButton)
        if (itemCount > 0) {
            Toast.makeText(this, "Items en el carrito: $itemCount", Toast.LENGTH_SHORT).show()
        }
    }
}