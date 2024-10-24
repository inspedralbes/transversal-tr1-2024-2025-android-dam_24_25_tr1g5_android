package com.example.apptakeaway

import android.content.Context
import com.example.apptakeaway.model.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ProductRepository(private val context: Context) {
    fun getProducts(): List<Product> {
        val jsonString = context.assets.open("products.json").bufferedReader().use { it.readText() }
        val type = object : com.google.gson.reflect.TypeToken<List<Product>>() {}.type
        return Gson().fromJson(jsonString, type)
    }
}