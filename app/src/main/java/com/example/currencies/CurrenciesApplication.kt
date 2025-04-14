package com.example.currencies

import android.app.Application
import android.content.res.Resources
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CurrenciesApplication : Application() {

    companion object {
        private lateinit var instance: CurrenciesApplication

        fun getResources(): Resources {
            return instance.resources
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
