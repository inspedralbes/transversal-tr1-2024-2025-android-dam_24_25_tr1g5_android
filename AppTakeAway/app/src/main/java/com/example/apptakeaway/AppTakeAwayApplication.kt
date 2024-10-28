package com.example.apptakeaway // Paquete donde se encuentra la aplicación

/*
 * La clase `AppTakeAwayApplication` es la clase principal de la aplicación que extiende `Application`.
 * En este punto, se inicializa el ViewModel `CartViewModel`, que se utilizará para gestionar la lógica
 * del carrito de compras a lo largo de la aplicación. Al extender `Application`, se asegura que el
 * ViewModel se mantenga en memoria durante toda la vida de la aplicación, permitiendo que los datos
 * del carrito persistan entre diferentes actividades y fragmentos.
 */

import android.app.Application // Importa la clase Application para crear la clase principal de la aplicación
import androidx.lifecycle.ViewModelProvider // Importa el proveedor de ViewModel para instanciar ViewModels
import com.example.apptakeaway.viewmodel.CartViewModel // Importa el ViewModel del carrito

// Clase principal de la aplicación
class AppTakeAwayApplication : Application() {
    lateinit var cartViewModel: CartViewModel // Declaración del ViewModel del carrito

    // Método que se llama al crear la aplicación
    override fun onCreate() {
        super.onCreate() // Llama al método de la clase base
        // Inicializa el ViewModel del carrito usando el ViewModelProvider
        cartViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this)
            .create(CartViewModel::class.java) // Crea una instancia del CartViewModel
    }
}
