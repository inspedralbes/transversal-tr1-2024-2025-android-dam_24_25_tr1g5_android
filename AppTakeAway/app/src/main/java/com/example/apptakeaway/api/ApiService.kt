package com.example.apptakeaway.api

import com.example.apptakeaway.model.Product
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("products")
    fun getProducts(): Call<List<Product>>
}