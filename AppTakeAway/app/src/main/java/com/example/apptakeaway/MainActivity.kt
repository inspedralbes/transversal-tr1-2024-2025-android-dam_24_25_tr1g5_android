package com.example.apptakeaway

import GridSpacingItemDecoration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.example.apptakeaway.adapter.ProductAdapter
import com.example.apptakeaway.viewmodel.ProductViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var viewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        setupRecyclerView()
        setupSearchView()
        observeProducts()

        viewModel.loadProducts(this)
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)
        val layoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(GridSpacingItemDecoration(2, 16, true))
        productAdapter = ProductAdapter(emptyList()) { product ->

            Toast.makeText(this, "${product.name} añadido al carrito", Toast.LENGTH_SHORT).show()

        }
        recyclerView.adapter = productAdapter
    }


    private fun setupSearchView() {
        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.filterProducts(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.filterProducts(newText)
                return true
            }
        })
    }

    private fun observeProducts() {
        viewModel.products.observe(this) { products ->
            productAdapter.updateProducts(products)
        }
    }

    private fun setupCartButton() {
        val cartButton = findViewById<ImageButton>(R.id.cartButton)
        cartButton.setOnClickListener {
            // Aquí puedes navegar a la actividad o fragmento del carrito
            // Por ejemplo:
            // startActivity(Intent(this, CartActivity::class.java))
            Toast.makeText(this, "Ir al carrito", Toast.LENGTH_SHORT).show()
        }
    }
}