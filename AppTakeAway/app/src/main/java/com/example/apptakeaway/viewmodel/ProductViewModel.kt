package com.example.apptakeaway.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.apptakeaway.model.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.content.Context

class ProductViewModel : ViewModel() {
    private val _products = MutableLiveData<List<Product>>()
    val products: MutableLiveData<List<Product>> = _products

    private var originalProducts: List<Product> = emptyList()

    fun loadProducts(context: Context) {
        val jsonString = context.assets.open("products.json").bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<Product>>() {}.type
        originalProducts = Gson().fromJson(jsonString, type)
        _products.value = originalProducts
    }

    fun filterProducts(query: String?) {
        if (query.isNullOrEmpty()) {
            _products.value = originalProducts
        } else {
            _products.value = originalProducts.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }
    }
}