package com.example.apptakeaway // Paquete donde se encuentra la clase de decoración para el RecyclerView

/*
 * La clase `GridSpacingItemDecoration` es una implementación de `RecyclerView.ItemDecoration`
 * que se utiliza para añadir un espaciado entre los elementos de un `RecyclerView` que utiliza un
 * diseño de cuadrícula (grid layout). Permite personalizar el espaciado entre los elementos,
 * incluyendo la opción de incluir márgenes en los bordes del RecyclerView.
 */

import android.graphics.Rect // Importa la clase Rect para definir áreas rectangulares
import android.view.View // Importa la clase View para manejar vistas en el layout
import androidx.recyclerview.widget.RecyclerView // Importa la clase RecyclerView para gestionar listas

// Clase para añadir espaciado en un RecyclerView
class GridSpacingItemDecoration(
    private val spanCount: Int, // Número de columnas en el diseño de cuadrícula
    private val spacing: Int, // Espacio a aplicar entre elementos
    private val includeEdge: Boolean // Si se deben incluir márgenes en los bordes
) : RecyclerView.ItemDecoration() { // Extiende la clase ItemDecoration de RecyclerView

    // Método que se llama para definir los offsets de cada elemento
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view) // Obtiene la posición del item en el adaptador
        val column = position % spanCount // Calcula la columna del item actual

        if (includeEdge) { // Si se incluyen márgenes en los bordes
            outRect.left = spacing - column * spacing / spanCount // Espaciado a la izquierda
            outRect.right = (column + 1) * spacing / spanCount // Espaciado a la derecha

            if (position < spanCount) { // Si es la fila superior
                outRect.top = spacing // Aplica espaciado en la parte superior
            }
            outRect.bottom = spacing // Aplica espaciado en la parte inferior
        } else { // Si no se incluyen márgenes en los bordes
            outRect.left = column * spacing / spanCount // Espaciado a la izquierda
            outRect.right = spacing - (column + 1) * spacing / spanCount // Espaciado a la derecha
            if (position >= spanCount) { // Si no está en la primera fila
                outRect.top = spacing // Aplica espaciado en la parte superior
            }
        }
    }
}
