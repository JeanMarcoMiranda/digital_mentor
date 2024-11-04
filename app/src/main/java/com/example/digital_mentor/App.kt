package com.example.digital_mentor

import android.app.Application
import com.example.digital_mentor.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()

        // Start koin
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(
                appModule
            ))
        }
    }
}