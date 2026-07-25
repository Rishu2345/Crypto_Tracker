package com.buildsol.cryptotracker.di

import com.buildsol.cryptotracker.data.repository.CoinRepositoryImpl
import com.buildsol.cryptotracker.domain.repository.CoinRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<CoinRepository> {
        CoinRepositoryImpl(
            coinGeckoApi = get(),
            coinDao = get(),
            coinIndex = get()
        )
    }

}