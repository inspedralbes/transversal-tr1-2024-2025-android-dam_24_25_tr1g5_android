package com.example.apptakeaway

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.example.apptakeaway.viewmodel.CartViewModel

class AppTakeAwayApplication : Application() {
    lateinit var cartViewModel: CartViewModel

    override fun onCreate() {
        super.onCreate()
        cartViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this).create(CartViewModel::class.java)
    }
}