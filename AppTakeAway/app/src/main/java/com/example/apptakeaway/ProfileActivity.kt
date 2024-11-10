package com.example.apptakeaway

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.apptakeaway.api.RetrofitClient
import com.example.apptakeaway.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileActivity : AppCompatActivity() {

    private lateinit var btnEditProfile: Button
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val isLoggedIn = intent.getBooleanExtra("isLoggedIn", false)

        val idTextView = findViewById<TextView>(R.id.idTextView)
        val emailTextView = findViewById<TextView>(R.id.emailTextView)
        val passwordTextView = findViewById<TextView>(R.id.passwordTextView)
        val firstNameTextView = findViewById<TextView>(R.id.firstNameTextView)
        val lastNameTextView = findViewById<TextView>(R.id.lastNameTextView)
        val paymentMethodTextView = findViewById<TextView>(R.id.paymentMethodTextView)
        btnEditProfile = findViewById(R.id.btnEditProfile)

        setupBackButton()

        if (!isLoggedIn) {
            idTextView.text = "No heu iniciat sessió."
            return
        }

        userId = intent.getIntExtra("userId", -1)
        val email = intent.getStringExtra("email") ?: "N/A"
        val firstName = intent.getStringExtra("firstName") ?: "N/A"
        val lastName = intent.getStringExtra("lastName") ?: "N/A"
        val paymentMethod = intent.getByteExtra("paymentMethod", 0)

        idTextView.text = "ID: $userId"
        emailTextView.text = "Correu: $email"
        passwordTextView.text = "Contrasenya: [No se muestra por motivos de seguridad]"
        firstNameTextView.text = "Nom: $firstName"
        lastNameTextView.text = "Cognom: $lastName"
        paymentMethodTextView.text = "Mètode de pagament: $paymentMethod"

        btnEditProfile.setOnClickListener {
            showEditProfileDialog(firstName, lastName, email, paymentMethod)
        }
    }

    private fun showEditProfileDialog(firstName: String, lastName: String, email: String, paymentMethod: Byte) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_profile, null)
        val firstNameEditText = dialogView.findViewById<EditText>(R.id.editFirstName)
        val lastNameEditText = dialogView.findViewById<EditText>(R.id.editLastName)
        val emailEditText = dialogView.findViewById<EditText>(R.id.editEmail)
        val passwordEditText = dialogView.findViewById<EditText>(R.id.editPassword)

        firstNameEditText.setText(firstName)
        lastNameEditText.setText(lastName)
        emailEditText.setText(email)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Editar Perfil")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val updatedFirstName = firstNameEditText.text.toString().trim()
                val updatedLastName = lastNameEditText.text.toString().trim()
                val updatedEmail = emailEditText.text.toString().trim()
                val updatedPassword = passwordEditText.text.toString().trim()

                if (updatedFirstName.isNotEmpty() && updatedLastName.isNotEmpty() && updatedEmail.isNotEmpty() && updatedPassword.isNotEmpty()) {
                    val updatedUser = User(
                        id = userId,
                        email = updatedEmail,
                        password = updatedPassword,
                        firstName = updatedFirstName,
                        lastName = updatedLastName,
                        paymentMethod = paymentMethod
                    )
                    updateUserProfile(updatedUser)
                } else {
                    Toast.makeText(this, "Si us plau, completa tots els camps.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar") { _, _ -> hideKeyboard(dialogView) }
            .create()

        dialog.setOnDismissListener { hideKeyboard(dialogView) }

        dialog.show()
    }

    private fun updateUserProfile(user: User) {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.apiService.updateUser(user.id ?: -1, user).execute()
                }

                if (response.isSuccessful) {
                    Toast.makeText(this@ProfileActivity, "Usuari actualitzat amb èxit.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ProfileActivity, "Error en actualitzar el perfil.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ProfileActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.d("ProfileActivity", "Error en actualitzar el perfil: ${e.message}", e)
            }
        }
    }

    private fun setupBackButton() {
        findViewById<ImageView>(R.id.backButton).setOnClickListener {
            finish()


        }
    }

    private fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


}
