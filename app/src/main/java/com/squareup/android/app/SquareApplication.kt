package com.squareup.android.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class SquareApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize Timber
        Timber.plant(Timber.DebugTree())
    }
}