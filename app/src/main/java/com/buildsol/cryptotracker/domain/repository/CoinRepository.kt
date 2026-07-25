package com.buildsol.cryptotracker.domain.repository

import com.buildsol.cryptotracker.domain.model.Coin
import com.buildsol.cryptotracker.domain.model.CoinDetails
import com.buildsol.cryptotracker.domain.model.CoinIndexItem
import com.buildsol.cryptotracker.domain.model.MarketChart
import com.buildsol.cryptotracker.domain.model.SearchCoin

interface CoinRepository {
    suspend fun getMarket(page:Int): List<Coin>

    suspend fun getCoinDetails(coinId: String): CoinDetails

    suspend fun search(query: String): List<SearchCoin>

    suspend fun getMarketChart(coinId: String, days: Int): MarketChart

    suspend fun loadCoinIndex()

    fun autocomplete(query: String): List<CoinIndexItem>
}