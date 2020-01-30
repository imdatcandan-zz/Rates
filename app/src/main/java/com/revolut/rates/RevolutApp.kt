package com.revolut.rates

import android.app.Application
import com.revolut.rates.di.module
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class RevolutApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@RevolutApp)
            modules(module)
        }
    }
}