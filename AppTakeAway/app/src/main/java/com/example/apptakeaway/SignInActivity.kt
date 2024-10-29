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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInActivity : AppCompatActivity() {
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button

    private val allUsers = mutableListOf<User>()  // Lista para almacenar todos los datos de los usuarios
    private val emailPasswordList = mutableListOf<Pair<String, String>>() // Lista para almacenar solo email y password

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        loginButton = findViewById(R.id.loginButton)

        // Cargar usuarios desde la API al iniciar la actividad
        loadUsers()

        // Configurar el botón de inicio de sesión
        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor ingresa correo y contraseña", Toast.LENGTH_SHORT).show()
            } else {
                verifyLogin(email, password)
            }
        }
    }

    // Método para cargar usuarios desde la API
    private fun loadUsers() {
        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.apiService.getUsers().enqueue(object : Callback<List<User>> {
                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    if (response.isSuccessful) {
                        response.body()?.let { userList ->
                            allUsers.clear()
                            emailPasswordList.clear()

                            allUsers.addAll(userList)  // Guardar todos los usuarios en allUsers
                            emailPasswordList.addAll(userList.map { it.email to it.password })  // Guardar email y password en otra lista
                        }
                    } else {
                        Log.e("SignInActivity", "Error al obtener usuarios: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    Log.e("SignInActivity", "Error de red: ${t.message}")
                }
            })
        }
    }

    // Método para verificar los datos de inicio de sesión
    private fun verifyLogin(email: String, password: String) {
        val user = allUsers.find { it.email == email && it.password == password }

        if (user != null) {
            Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()

            // Crear intent para MainActivity y pasar los datos del usuario
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("userId", user.id)
                putExtra("email", user.email)
                putExtra("password", user.password)
                putExtra("firstName", user.firstName)
                putExtra("lastName", user.lastName)
                putExtra("typeUserId", user.typeUserId)
                putExtra("paymentMethod", user.paymentMethod)
            }
            startActivity(intent)
            finish() // Opcional: cerrar SignInActivity
        } else {
            Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
        }
    }
}
