package com.buildsol.cryptotracker

import android.app.Application
import com.buildsol.cryptotracker.di.databaseModule
import com.buildsol.cryptotracker.di.networkModule
import com.buildsol.cryptotracker.di.repositoryModule
import com.buildsol.cryptotracker.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        GlobalContext.startKoin {
            androidContext(this@MyApp)
            modules(
                databaseModule,
                repositoryModule,
                networkModule,
                viewModelModule
            )
        }
    }
}