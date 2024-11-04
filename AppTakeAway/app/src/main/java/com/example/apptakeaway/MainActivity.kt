package com.example.apptakeaway

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

    private var userId: Int = -1
    private var email: String = ""
    private var firstName: String = ""
    private var lastName: String = ""
    private var paymentMethod: Byte = 0
    private var isLoggedIn: Boolean = false // Variable para verificar si el usuario ha iniciado sesión

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Recuperar datos del Intent solo dentro del onCreate
        userId = intent.getIntExtra("userId", -1)
        email = intent.getStringExtra("email") ?: ""
        firstName = intent.getStringExtra("firstName") ?: ""
        lastName = intent.getStringExtra("lastName") ?: ""
        paymentMethod = intent.getByteExtra("paymentMethod", 0)

        // Recuperar el estado de inicio de sesión
        isLoggedIn = intent.getBooleanExtra("isLoggedIn", false)

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

        // Realizar acciones basadas en si el usuario ha iniciado sesión
        if (!isLoggedIn) {
            Toast.makeText(this, "No has iniciado sesión. Accediendo como invitado.", Toast.LENGTH_SHORT).show()
            // Aquí puedes implementar cualquier otra lógica que necesites para usuarios no autenticados.
        }
    }

    private fun setupUserButton() {
        val userButton = findViewById<ImageButton>(R.id.userButton)

        // Crear un objeto User con los datos del usuario
        val user = User(
            id = userId,
            email = email,
            password = "", // Password puede dejarse vacío o gestionarse de otra manera
            firstName = firstName,
            lastName = lastName,
            paymentMethod = paymentMethod // Usar el valor Byte
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
                    // Lógica para abrir el perfil...
                    val intent = Intent(this, ProfileActivity::class.java).apply {
                        putExtra("isLoggedIn", isLoggedIn)
                        putExtra("userId", user.id)
                        putExtra("email", user.email)
                        putExtra("firstName", user.firstName)
                        putExtra("lastName", user.lastName)
                    }
                    startActivity(intent)
                    true
                }
                R.id.pedidos -> {
                    Toast.makeText(this, "Pedidos seleccionada", Toast.LENGTH_SHORT).show()
                    // Iniciar OrdersActivity
                    val intent = Intent(this, OrderActivity::class.java)
                    intent.putExtra("USER_ID", user.id) // Pasar userId al OrderActivity
                    startActivity(intent)
                    true
                }
                R.id.logout -> {
                    // Lógica para cerrar sesión
                    Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                    // Aquí puedes limpiar los datos del usuario
                    clearUserData()
                    // Redirigir a la actividad de inicio de sesión o a la actividad principal
                    startActivity(Intent(this, InitialScreenActivity::class.java))
                    finish() // Finaliza la actividad actual
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }

    // Método para limpiar los datos del usuario
    private fun clearUserData() {
        userId = -1
        email = ""
        firstName = ""
        lastName = ""
        paymentMethod = 0
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
