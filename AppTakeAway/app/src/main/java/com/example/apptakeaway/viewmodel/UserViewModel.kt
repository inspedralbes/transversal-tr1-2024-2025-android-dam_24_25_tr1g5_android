// UserViewModel.kt
package com.example.apptakeaway.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.apptakeaway.api.RetrofitClient
import com.example.apptakeaway.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel() {
    // Almacena la lista completa de usuarios
    private val _allUsers = MutableLiveData<List<User>>()
    val allUsers: LiveData<List<User>> get() = _allUsers

    // Almacena solo el usuario autenticado
    private val _authenticatedUser = MutableLiveData<User?>()
    val authenticatedUser: LiveData<User?> get() = _authenticatedUser

    // Método para cargar todos los usuarios desde la API
    fun loadUsers() {
        RetrofitClient.apiService.getUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _allUsers.value = it
                    } ?: run {
                        Log.e("UserViewModel", "Lista de usuarios vacía")
                        _allUsers.value = emptyList()
                    }
                } else {
                    Log.e("UserViewModel", "Error en respuesta: ${response.code()}")
                    _allUsers.value = emptyList()
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.e("UserViewModel", "Error de red: ${t.message}")
                _allUsers.value = emptyList()
            }
        })
    }

    // Método para autenticar el usuario con correo y contraseña
    fun authenticateUser(email: String, password: String) {
        _allUsers.value?.let { users ->
            val user = users.find { it.email == email && it.password == password }
            if (user != null) {
                _authenticatedUser.value = user
                Log.d("UserViewModel", "Autenticación exitosa")
            } else {
                _authenticatedUser.value = null
                Log.d("UserViewModel", "Autenticación fallida")
            }
        }
    }
}
