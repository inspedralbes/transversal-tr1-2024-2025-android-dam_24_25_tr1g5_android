package com.example.apptakeaway

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        cartViewModel = ViewModelProvider(applicationContext as ViewModelStoreOwner).get(CartViewModel::class.java)

        setupRecyclerView()
        setupSearchView()
        setupCartButton()
        observeProducts()
        observeCart()

        productViewModel.loadProducts(this)
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)
        val layoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(GridSpacingItemDecoration(2, 16, true))
        productAdapter = ProductAdapter(emptyList()) { product ->
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
            productAdapter.updateProducts(products)
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
        }
    }

    private fun updateCartBadge(itemCount: Int) {
        val cartButton = findViewById<ImageButton>(R.id.cartButton)
        if (itemCount > 0) {
            Toast.makeText(this, "Items en el carrito: $itemCount", Toast.LENGTH_SHORT).show()
        }
    }

    class GridSpacingItemDecoration(private val spanCount: Int, private val spacing: Int, private val includeEdge: Boolean) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val position = parent.getChildAdapterPosition(view)
            val column = position % spanCount
            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount
                outRect.right = (column + 1) * spacing / spanCount
                if (position < spanCount) {
                    outRect.top = spacing
                }
                outRect.bottom = spacing
            } else {
                outRect.left = column * spacing / spanCount
                outRect.right = spacing - (column + 1) * spacing / spanCount
                if (position >= spanCount) {
                    outRect.top = spacing
                }
            }
        }
    }
}