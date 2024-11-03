package com.example.apptakeaway

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Recupera los datos del Intent
        val id = intent.getIntExtra("id", -1)
        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")
        val firstName = intent.getStringExtra("firstName")
        val lastName = intent.getStringExtra("lastName")
        val typeUserId = intent.getIntExtra("typeUserId", -1)
        val paymentMethod = intent.getStringExtra("paymentMethod")

        // Referencias a las vistas de texto en activity_profile.xml
        findViewById<TextView>(R.id.idTextView).text = "ID: $id"
        findViewById<TextView>(R.id.emailTextView).text = "Email: $email"
        findViewById<TextView>(R.id.passwordTextView).text = "Contraseña: $password"
        findViewById<TextView>(R.id.firstNameTextView).text = "Nombre: $firstName"
        findViewById<TextView>(R.id.lastNameTextView).text = "Apellido: $lastName"
        findViewById<TextView>(R.id.typeUserIdTextView).text = "Tipo de Usuario ID: $typeUserId"
        findViewById<TextView>(R.id.paymentMethodTextView).text = "Método de Pago: $paymentMethod"
    }
}
