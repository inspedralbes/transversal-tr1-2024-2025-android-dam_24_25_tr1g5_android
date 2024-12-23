package com.example.apptakeaway // Paquete donde se encuentra la clase MainActivity

/*
 * La clase `MainActivity` es la actividad principal de la aplicación, que presenta
 * una lista de productos disponibles en un formato de cuadrícula. Permite a los usuarios
 * buscar productos, añadirlos a un carrito y navegar a la pantalla del carrito.
 * Observa cambios en el ViewModel de productos y el ViewModel del carrito,
 * actualizando la interfaz de usuario en consecuencia.
 */
import com.example.apptakeaway.OrderActivity
import android.content.Intent // Importa Intent para navegar entre actividades
import android.os.Bundle // Importa Bundle para pasar datos entre actividades
import android.util.Log // Importa Log para registrar información de depuración
import android.view.View // Importa View para manejar vistas
import android.widget.ImageButton // Importa ImageButton para manejar botones de imagen
import android.widget.PopupMenu
import android.widget.ProgressBar // Importa ProgressBar para mostrar carga
import android.widget.TextView
import android.widget.Toast // Importa Toast para mostrar mensajes breves
import androidx.appcompat.app.AppCompatActivity // Importa AppCompatActivity para la actividad base
import androidx.appcompat.widget.SearchView // Importa SearchView para búsqueda de productos
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager // Importa GridLayoutManager para el diseño en cuadrícula
import androidx.recyclerview.widget.RecyclerView // Importa RecyclerView para listas de elementos
import com.example.apptakeaway.adapter.ProductAdapter // Importa el adaptador para productos
import com.example.apptakeaway.api.SocketManager
import com.example.apptakeaway.model.Order
import com.example.apptakeaway.model.Product
import com.example.apptakeaway.model.User
import com.example.apptakeaway.viewmodel.CartViewModel // Importa el ViewModel del carrito
import com.example.apptakeaway.viewmodel.ProductViewModel // Importa el ViewModel de productos
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.socket.emitter.Emitter
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONString

class MainActivity : AppCompatActivity() { // Clase principal de la actividad
    private lateinit var recyclerView: RecyclerView // Vista para la lista de productos
    private lateinit var productAdapter: ProductAdapter // Adaptador para manejar los productos
    private lateinit var productViewModel: ProductViewModel // ViewModel para manejar la lógica de productos
    private lateinit var cartViewModel: CartViewModel // ViewModel para manejar el carrito
    private lateinit var progressBar: ProgressBar // Barra de progreso para mostrar carga de datos
    private val socket = SocketManager.getSocket()


    private var userId: Int = -1
    private var email: String = ""
    private var firstName: String = ""
    private var lastName: String = ""
    private var paymentMethod: Byte = 0
    private var isLoggedIn: Boolean = false // Variable para verificar si el usuario ha iniciado sesión

    // Método que se llama al crear la actividad
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
        productViewModel = ProductViewModel() // Inicializa el ViewModel de productos
        cartViewModel = (application as AppTakeAwayApplication).cartViewModel // Obtiene el ViewModel del carrito


        socket.connect()

        // Escuchar eventos: orders y products
        socket.on("products", onProducts)



        setupRecyclerView() // Configura el RecyclerView
        setupSearchView() // Configura la vista de búsqueda
        setupCartButton() // Configura el botón del carrito
        setupUserButton()
        observeProducts() // Observa cambios en los productos
        observeCart() // Observa cambios en el carrito
        observePayItems() // Observa cambios en payItems

        progressBar = findViewById(R.id.progressBar) // Inicializa la barra de progreso


        loadProducts()

        // Realizar acciones basadas en si el usuario ha iniciado sesión
        if (!isLoggedIn) {
            Toast.makeText(this, "No heu iniciat sessió. Accedint com a convidat.", Toast.LENGTH_SHORT).show()
            // Aquí puedes implementar cualquier otra lógica que necesites para usuarios no autenticados.
        }
    }



    // Listener para el evento "products"
    private val onProducts = Emitter.Listener { args ->
        val data = args[0] as String
        Log.d("SocketEvent", "Actualització de productes: $data")
        // Aquí puedes manejar los datos y actualizar la UI
        val gson = Gson()
        val listType = object : TypeToken<List<Product>>() {}.type
        val updateProduct: List<Product> = gson.fromJson(data, listType)

        updateProducts(updateProduct)
    }

    override fun onDestroy() {
        super.onDestroy()

        // Desconectar los listeners para evitar fugas de memoria

        socket.off("products", onProducts)

        // Desconectar del socket cuando la actividad se destruye
        socket.disconnect()
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
                // Dentro del listener de opciones del popup para pedidos
                R.id.pedidos -> {
                    Toast.makeText(this, "Comandes seleccionada", Toast.LENGTH_SHORT).show()
                    // Iniciar OrderActivity con el userId
                    val intent = Intent(this, OrderActivity::class.java)
                    intent.putExtra("userId", user.id) // Usa "userId" como clave aquí también
                    startActivity(intent)
                    true
                }

                R.id.logout -> {
                    // Lógica para cerrar sesión
                    Toast.makeText(this, "Sessió tancada", Toast.LENGTH_SHORT).show()
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
        progressBar.visibility = View.VISIBLE // Muestra la barra de progreso
        productViewModel.loadProducts() // Llama al método de ViewModel para cargar productos
    }

    // Método para configurar el RecyclerView
    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView) // Obtiene la referencia del RecyclerView
        val layoutManager = GridLayoutManager(this, 2) // Define el layout en cuadrícula con 2 columnas
        recyclerView.layoutManager = layoutManager // Asigna el layout manager al RecyclerView

        // Inicializa el adaptador de productos
        productAdapter = ProductAdapter { product ->
            cartViewModel.addToCart(product) // Añade el producto al carrito
            Log.d("MainActivity", "Producte afegit a la cistella: ${product.name}") // Log de depuración
            Toast.makeText(this, "${product.name} añadido al carrito", Toast.LENGTH_SHORT).show() // Mensaje al usuario
        }

        recyclerView.adapter = productAdapter // Asigna el adaptador al RecyclerView
    }

    // Método para configurar la vista de búsqueda
    private fun setupSearchView() {
        val searchView = findViewById<SearchView>(R.id.searchView) // Obtiene la referencia de la vista de búsqueda

        // Establece un listener para manejar las consultas de texto
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                productViewModel.filterProducts(query) // Filtra productos cuando se envía la consulta
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                productViewModel.filterProducts(newText) // Filtra productos a medida que cambia el texto
                return true
            }
        })
    }

    // Método para observar cambios en la lista de productos
    private fun observeProducts() {
    productViewModel.products.observe(this) { products -> // Observa el LiveData de productos
    progressBar.visibility = View.GONE // Oculta la barra de progreso al cargar productos

            if (products.isNotEmpty()) {
                productAdapter.submitList(products) // Actualiza el adaptador con la lista de productos
                Log.d("MainActivity", "Productes carregats: ${products.size}") // Log de depuración
            } else {
                Toast.makeText(this, "No s'han trobat productes", Toast.LENGTH_SHORT).show() // Mensaje si no hay productos
                Log.d("MainActivity", "No s'han trobat productes") // Log de depuración
            }
        }
}
    private fun updateProducts(products:List<Product>) {
        if (products.isNotEmpty()) {
            productAdapter.submitList(products) // Actualiza el adaptador con la lista de productos
            Log.d("MainActivity", "Productes carregats: ${products.size}") // Log de depuración
        } else {
            Toast.makeText(this, "No s'han trobat productes", Toast.LENGTH_SHORT).show() // Mensaje si no hay productos
            Log.d("MainActivity", "No s'han trobat productes") // Log de depuración
        }
    }


    // Método para configurar el botón del carrito
    private fun setupCartButton() {
        val cartButton = findViewById<ImageButton>(R.id.cartButton) // Obtiene el botón del carrito

        // Establece un listener para manejar el clic en el botón
        cartButton.setOnClickListener {
            Log.d("MainActivity", "Navegant a CartActivity") // Log de depuración

            // Crea el Intent para CartActivity y pasa el userId
            val intent = Intent(this, CartActivity::class.java)
            intent.putExtra("userId", userId) // Pasa el userId al carrito
            intent.putExtra("isLoggedIn", isLoggedIn)

            // Navega a la actividad del carrito
            startActivity(intent)
        }
    }


    // Método para observar cambios en el carrito
    private fun observeCart() {
        cartViewModel.cartItems.observe(this) { cartItems -> // Observa el LiveData de items en el carrito
            updateCartBadge(cartItems.sumOf { it.quantity }) // Actualiza la insignia del carrito con el conteo de items
            Log.d("MainActivity", "Cistella actualitzada: ${cartItems.size} items") // Log de depuración
        }
    }

    // Método para observar cambios en payItems
    private fun observePayItems() {
        cartViewModel.payItems.observe(this) { payItems ->
            Log.d("MainActivity", "PayItems actualitzada: ${payItems.size} items") // Log de depuración
        }
    }

    // Método para actualizar la insignia del carrito
    private fun updateCartBadge(itemCount: Int) {
        val cartButton = findViewById<ImageButton>(R.id.cartButton) // Obtiene el botón del carrito
        val cartItemCount = findViewById<TextView>(R.id.cartItemCount) // Obtiene el TextView con el número de items

        if (itemCount > 0) {
            cartItemCount.text = itemCount.toString() // Actualiza el texto con el número de items
            cartItemCount.visibility = View.VISIBLE // Muestra el TextView
        } else {
            cartItemCount.visibility = View.GONE // Oculta el TextView si no hay items
        }

        // Log de depuración para verificar los cambios
        Log.d("MainActivity", "Cistella actualitzada amb $itemCount items")
    }

}
