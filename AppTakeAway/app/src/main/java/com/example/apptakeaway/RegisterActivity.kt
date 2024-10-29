// RegisterActivity.kt
package com.example.apptakeaway

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.apptakeaway.api.RetrofitClient
import com.example.apptakeaway.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inicializar las vistas
        emailEditText = findViewById(R.id.emailEditText)
        firstNameEditText = findViewById(R.id.firstNameEditText)
        lastNameEditText = findViewById(R.id.lastNameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        registerButton = findViewById(R.id.registerButton)

        // Configurar el botón de registro
        registerButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val firstName = firstNameEditText.text.toString().trim()
            val lastName = lastNameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Validar la entrada del usuario
            if (validateInput(email, password)) {
                // Crear un objeto User usando el data class
                val user = User(
                    email = email,
                    password = password,
                    firstName = if (firstName.isNotEmpty()) firstName else "",
                    lastName = if (lastName.isNotEmpty()) lastName else "",
                    paymentMethod = null // Asumiendo que no se necesita de momento
                )

                // Registrar el usuario
                registerUser(user)
            }
        }
    }

    // Método para validar la entrada del usuario
    private fun validateInput(email: String, password: String): Boolean {
        var isValid = true

        if (email.isEmpty()) {
            emailEditText.error = "El correo es obligatorio"
            isValid = false
        }

        if (password.isEmpty()) {
            passwordEditText.error = "La contraseña es obligatoria"
            isValid = false
        }

        return isValid
    }

    // Método para registrar el usuario usando Retrofit
    private fun registerUser(user: User) {
        RetrofitClient.apiService.postUser(user).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@RegisterActivity, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@RegisterActivity, SignInActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@RegisterActivity, "Error en el registro, intenta de nuevo", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "Fallo en la conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
