package com.example.apptakeaway

import android.app.Application
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

class AppTakeAwayApplication : Application(), ViewModelStoreOwner {
    private val appViewModelStore: ViewModelStore by lazy {
        ViewModelStore()
    }

    override val viewModelStore: ViewModelStore
        get() = appViewModelStore

}