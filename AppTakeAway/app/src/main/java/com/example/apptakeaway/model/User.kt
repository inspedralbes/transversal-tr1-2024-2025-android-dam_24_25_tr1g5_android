package com.example.apptakeaway.model

// Definición del data class User
data class User(
    val id: Int? = null,               // Autoincrementado; null para indicar que es opcional
    val email: String,                 // Campo obligatorio
    val password: String,              // Campo obligatorio
    val firstName: String? = null,     // Campo opcional, puede ser null
    val lastName: String? = null,      // Campo opcional, puede ser null
    val paymentMethod: Boolean? = null  // Representación de TINYINT(1); puede ser true/false o null
)

