package com.example.apptakeaway.model

import java.io.Serializable

// Paquete donde se encuentra el modelo del producto

/*
 * La clase de datos `Product` representa un producto disponible en la aplicación.
 * Contiene todos los atributos relevantes para describir un producto, como su nombre,
 * descripción, precio, stock disponible y otros detalles. Esta clase se utiliza para
 * manejar la información de los productos que se mostrarán en el catálogo de la aplicación.
 */

// Clase de datos que representa un producto
data class Product(
    val id: Int, // Identificador único del producto
    val categoryId: Int, // Identificador de la categoría a la que pertenece el producto
    val name: String, // Nombre del producto
    val description: String, // Descripción detallada del producto
    val size: String, // Tamaño del producto (si aplica)
    val price: Double, // Precio del producto
    val imagePath: String, // Ruta de la imagen del producto
    val colors: String, // Colores disponibles para el producto
    val stock: Int, // Cantidad disponible en stock
    val activated: Int // Estado de activación del producto (por ejemplo, si está disponible para la compra)
)
