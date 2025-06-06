package com.example.starzplay_videoappcodechallenge.application

import android.app.Application
import com.example.library.di.libraryModule
import com.example.starzplay_videoappcodechallenge.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

// Custom Application class for initializing global configurations
class App : Application() {

    // This method is called when the application is starting
    override fun onCreate() {
        super.onCreate()
        // Initialize Koin (Dependency Injection framework)
        startKoin {
            // Provide the Android application context to Koin
            androidContext(this@App)
            // Load the Koin modules containing dependencies
            modules(
                libraryModule, // External/shared library
                appModule      // App-specific
            )
        }
    }
}
