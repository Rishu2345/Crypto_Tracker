package com.buildsol.cryptotracker.di

import com.buildsol.cryptotracker.presentation.details.CoinDetailViewModel
import com.buildsol.cryptotracker.presentation.coinList.CoinListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::CoinListViewModel)
    viewModelOf(::CoinDetailViewModel)
}