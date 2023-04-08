package com.example.catscompose

import android.app.Application
import com.example.catscompose.BuildConfig
import timber.log.Timber

class CatsApp: Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Timber.d("Timber initialized")
        }
    }
}