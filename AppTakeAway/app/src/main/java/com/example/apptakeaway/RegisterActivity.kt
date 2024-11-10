// RegisterActivity.kt
package com.example.apptakeaway

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

            // Validar entrada del usuario antes de registrar
            if (validateInput(email, password)) {
                val user = User(
                    email = email,
                    password = password,
                    firstName = if (firstName.isNotEmpty()) firstName else "",
                    lastName = if (lastName.isNotEmpty()) lastName else "",
                    paymentMethod = 0
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
            emailEditText.error = "El correu és obligatori"
            isValid = false
        } else if (!isValidEmail(email)) { // Validar el formato del correo electrónico
            emailEditText.error = "El format del correu no és vàlid"
            isValid = false
        }

        if (password.isEmpty()) {
            passwordEditText.error = "La contrasenya és obligatòria"
            isValid = false
        }

        return isValid
    }

    // Método para validar el formato del correo electrónico
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Método para registrar al usuario usando Retrofit
    private fun registerUser(user: User) {
        Log.d("RegisterActivity", "Trucant al mètode postUser a ApiService")
        RetrofitClient.apiService.postUser(user).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@RegisterActivity, "Usuari registrat correctament", Toast.LENGTH_SHORT).show()

                    // Redirigir a SignInActivity al registrarse exitosamente
                    startActivity(Intent(this@RegisterActivity, SignInActivity::class.java))
                    finish() // Finalizar la actividad actual
                } else {
                    Log.d("RegisterActivity", "Error al registre: ${response.code()}")
                    Toast.makeText(this@RegisterActivity, "Error al registre, intenta de nou", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("RegisterActivity", "Error de xarxa: ${t.message}")
                Toast.makeText(this@RegisterActivity, "Error de xarxa: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
