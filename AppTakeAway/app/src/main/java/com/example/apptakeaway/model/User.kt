package com.example.apptakeaway.model

// Definición del data class User
data class User(
    val id: Int = 6,               // Campo autoincremental
    val typeUserId: Int? = 1,           // ID de tipo de usuario, con valor predeterminado de 1
    val email: String,                  // Campo obligatorio para el correo electrónico
    val password: String,               // Campo obligatorio para la contraseña
    val firstName: String? = null,      // Campo opcional para el primer nombre
    val lastName: String? = null,       // Campo opcional para el apellido
    val paymentMethod: Byte  // Representación de TINYINT(1), acepta valores nulos
)