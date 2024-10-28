package com.example.apptakeaway // Paquete donde se encuentra la clase ProductDiffCallback

/*
 * La clase `ProductDiffCallback` extiende `DiffUtil.Callback` para ayudar en la
 * actualización eficiente de listas en un `RecyclerView`. Esta clase calcula las
 * diferencias entre una lista antigua y una nueva de productos, optimizando
 * las actualizaciones para evitar la recreación innecesaria de elementos.
 */

import androidx.recyclerview.widget.DiffUtil // Importa DiffUtil para la comparación de listas
import com.example.apptakeaway.model.Product // Importa el modelo de datos Product

class ProductDiffCallback(
    private val oldList: List<Product>, // Lista antigua de productos
    private val newList: List<Product> // Nueva lista de productos
) : DiffUtil.Callback() { // Extiende DiffUtil.Callback para comparar listas
    // Devuelve el tamaño de la lista antigua
    override fun getOldListSize() = oldList.size

    // Devuelve el tamaño de la nueva lista
    override fun getNewListSize() = newList.size

    // Compara si dos elementos son el mismo (basado en el ID del producto)
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    // Compara si el contenido de dos elementos es el mismo
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]
}
