package com.buildsol.cryptotracker

import android.app.Application
import com.buildsol.cryptotracker.di.appKoinModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        GlobalContext.startKoin {
            androidContext(this@MyApp)
            modules(appKoinModule)
        }
    }
}