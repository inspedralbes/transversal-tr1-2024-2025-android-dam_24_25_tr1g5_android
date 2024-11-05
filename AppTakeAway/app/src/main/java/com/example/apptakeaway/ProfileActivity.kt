package com.example.apptakeaway

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Recupera los datos del Intent
        val isLoggedIn = intent.getBooleanExtra("isLoggedIn", false)

        // Referencias a las vistas de texto en activity_profile.xml
        val idTextView = findViewById<TextView>(R.id.idTextView)
        val emailTextView = findViewById<TextView>(R.id.emailTextView)
        val passwordTextView = findViewById<TextView>(R.id.passwordTextView)
        val firstNameTextView = findViewById<TextView>(R.id.firstNameTextView)
        val lastNameTextView = findViewById<TextView>(R.id.lastNameTextView)
        val paymentMethodTextView = findViewById<TextView>(R.id.paymentMethodTextView)

        setupBackButton()
        // Si no está logueado, muestra un mensaje
        if (!isLoggedIn) {
            idTextView.text = "No has iniciado sesión."
            return // Salir de la actividad
        }


        // Si está logueado, recupera los datos del usuario
        val id = intent.getIntExtra("userId", -1)
        val email = intent.getStringExtra("email") ?: "N/A"
        // Nota: No deberías mostrar la contraseña. Se sugiere omitirla por razones de seguridad.
        val firstName = intent.getStringExtra("firstName") ?: "N/A"
        val lastName = intent.getStringExtra("lastName") ?: "N/A"
        val paymentMethod = intent.getByteExtra("paymentMethod", 0)

        // Muestra los datos del usuario
        idTextView.text = "ID: $id"
        emailTextView.text = "Email: $email"
        passwordTextView.text = "Contraseña: [No se muestra por motivos de seguridad]" // Omitido
        firstNameTextView.text = "Nombre: $firstName"
        lastNameTextView.text = "Apellido: $lastName"
        paymentMethodTextView.text = "Método de Pago: $paymentMethod"



    }

    fun setupBackButton() {
        findViewById<ImageView>(R.id.backButton).setOnClickListener {
            finish()
        }
    }
}
