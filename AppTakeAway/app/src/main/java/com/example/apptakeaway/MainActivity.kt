package com.example.apptakeaway // Paquete donde se encuentra la clase MainActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apptakeaway.adapter.ProductAdapter
import com.example.apptakeaway.model.User
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
        setupUserButton() // Botón de usuario para mostrar el menú y perfil
        observeProducts()
        observeCart()

        progressBar = findViewById(R.id.progressBar)
        loadProducts()
    }

    private fun setupUserButton() {
        val userButton = findViewById<ImageButton>(R.id.userButton)

        // Crear un objeto User con los datos del usuario
        val user = User(
            id = 1,
            email = "juan@example.com",
            password = "1234",
            firstName = "Juan",
            lastName = "Pérez",
            typeUserId = 2,
            paymentMethod = "Tarjeta de Crédito"
        )

        // Establece un listener para mostrar el PopupMenu y manejar opciones de usuario
        userButton.setOnClickListener { view ->
            showUserMenu(view, user)
        }
    }

    // Método para mostrar el PopupMenu del usuario
    private fun showUserMenu(view: View, user: User) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.user_menu, popupMenu.menu)

        // Listener para manejar las opciones seleccionadas
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.profile -> {
                    Toast.makeText(this, "Perfil seleccionado", Toast.LENGTH_SHORT).show()
                    // Crear el intent para iniciar ProfileActivity
                    val intent = Intent(this, ProfileActivity::class.java)

                    // Pasar cada atributo del objeto User como extra
                    intent.putExtra("id", user.id)
                    intent.putExtra("email", user.email)
                    intent.putExtra("password", user.password)
                    intent.putExtra("firstName", user.firstName)
                    intent.putExtra("lastName", user.lastName)
                    intent.putExtra("typeUserId", user.typeUserId)
                    intent.putExtra("paymentMethod", user.paymentMethod)

                    startActivity(intent)
                    true
                }
                R.id.pedidos -> {
                    Toast.makeText(this, "Configuración seleccionada", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.logout -> {
                    Toast.makeText(this, "Cerrar sesión", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }

    // Método para cargar los productos
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
