package com.example.apptakeaway.api

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitClient {
    private const val BASE_URL = "file:/"

    fun create(context: Context): ApiService {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val url = originalRequest.url.toString()
                val newUrl = url.replace(BASE_URL, "")
                val newRequest = originalRequest.newBuilder()
                    .url(newUrl)
                    .build()
                val response = chain.proceed(newRequest)
                response.newBuilder()
                    .body(response.body?.let {
                        context.assets.open(newUrl).use { inputStream ->
                            okhttp3.ResponseBody.create(
                                response.body?.contentType(),
                                inputStream.readBytes()
                            )
                        }
                    })
                    .build()
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}