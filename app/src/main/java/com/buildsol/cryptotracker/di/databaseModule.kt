package com.buildsol.cryptotracker.di

import androidx.room.Room
import com.buildsol.cryptotracker.data.local.database.AppDatabase
import com.buildsol.cryptotracker.data.local.database.CoinIndex
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "crypto_tracker.db"
        ).build()
    }

    single {
        get<AppDatabase>().coinDao()
    }

    single {
        CoinIndex()
    }

}

