package com.example.digital_mentor

import android.app.Application
import com.example.digital_mentor.core.di.DataModule
import com.example.digital_mentor.core.di.NetworkModule
import com.example.digital_mentor.core.di.ViewModelModule
import com.example.digital_mentor.core.di.appModule
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