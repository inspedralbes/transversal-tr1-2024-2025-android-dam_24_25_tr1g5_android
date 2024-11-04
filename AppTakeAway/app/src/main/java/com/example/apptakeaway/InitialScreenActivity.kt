package com.example.apptakeaway

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class InitialScreenActivity : AppCompatActivity() {

    // Variable que indica si el usuario ha iniciado sesión
    private var isLoggedIn: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initialscreen) // Establece el layout de la pantalla inicial

        val guestButton = findViewById<Button>(R.id.guestButton)
        guestButton.setOnClickListener {
            // El usuario entra como invitado
            isLoggedIn = false // Marcamos que no ha iniciado sesión
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("isLoggedIn", isLoggedIn) // Pasamos la variable a la siguiente actividad
            startActivity(intent)
            finish()
        }

        val registerButton = findViewById<Button>(R.id.registerButton)
        registerButton.setOnClickListener {
            // El usuario inicia sesión
            isLoggedIn = true // Marcamos que ha iniciado sesión
            val intent = Intent(this, SignInActivity::class.java)
            intent.putExtra("isLoggedIn", true) // Pasamos la variable a la siguiente actividad
            startActivity(intent)
            finish()
        }
    }
}
