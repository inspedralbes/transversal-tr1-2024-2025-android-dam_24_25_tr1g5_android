package com.example.apptakeaway.api

import com.example.apptakeaway.model.Product
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("productUser")
    fun getProducts(): Call<List<Product>>
}