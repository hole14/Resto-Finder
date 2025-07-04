package com.example.restofinder

import android.app.Application
import com.google.android.libraries.places.api.Places

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.api_key))
        }
    }
}