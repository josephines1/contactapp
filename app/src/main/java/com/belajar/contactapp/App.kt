package com.belajar.contactapp

import android.app.Application
import com.belajar.contactapp.di.firebaseModule
import com.belajar.contactapp.di.repositoryModule
import com.belajar.contactapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(
                viewModelModule,
                repositoryModule,
                firebaseModule
            ))
        }
    }
}