// SignInActivity.kt
package com.example.apptakeaway

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.apptakeaway.model.Login
import com.example.apptakeaway.api.RetrofitClient
import com.example.apptakeaway.model.User
import org.mindrot.jbcrypt.BCrypt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInActivity : AppCompatActivity() {
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var registerText: TextView // Corrige el nombre a 'registerText'

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // Inicializar vistas
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        loginButton = findViewById(R.id.loginButton)
        registerText = findViewById(R.id.registerText)  // Corrige aquí

        // Configurar el botón de inicio de sesión
        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Si us plau introduïu correu i contrasenya", Toast.LENGTH_SHORT).show()
            } else {
                Log.e(password, "hashpassowrd")
                loginUser(email, password)
            }
        }

        // Configurar el texto de registro para que redirija a la pantalla de registro
        registerText.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)  // Cambia a la actividad de registro
            startActivity(intent)
        }
    }

    // Método para autenticar al usuario
    private fun loginUser(email: String, password: String) {
        // Crear el objeto de solicitud de inicio de sesión
        //val loginUser = Login(userLogin)
        val datauser = Login(email, password)
        // Realizar la solicitud a través de Retrofit
        RetrofitClient.apiService.login(datauser).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful && response.body() != null) {
                    val user = response.body()!!
                    Toast.makeText(
                        this@SignInActivity,
                        "Inici de sessió reeixit",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Crear intent para MainActivity y pasar los datos del usuario
                    // Crear intent para MainActivity y pasar los datos del usuario
                    val intent = Intent(this@SignInActivity, MainActivity::class.java).apply {
                        putExtra("isLoggedIn", true) // Agrega esta línea para pasar el estado de inicio de sesión
                        putExtra("userId", user.id)
                        putExtra("email", user.email)
                        putExtra("firstName", user.firstName)
                        putExtra("lastName", user.lastName)
                        putExtra("paymentMethod", user.paymentMethod)
                    }
                    startActivity(intent)
                    finish()

                } else {
                    Toast.makeText(this@SignInActivity, "Usuari no trobat", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(
                    this@SignInActivity,
                    "Error de xarxa: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("SignInActivity", "Error de xarxa", t)
            }
        })
    }
}
