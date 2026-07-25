package com.buildsol.cryptotracker.data.repository

import com.buildsol.cryptotracker.data.local.dao.CoinDao
import com.buildsol.cryptotracker.data.local.database.CoinIndex
import com.buildsol.cryptotracker.data.remote.api.CoinGeckoApi
import com.buildsol.cryptotracker.data.remote.mapper.toDomain
import com.buildsol.cryptotracker.data.remote.mapper.toEntity
import com.buildsol.cryptotracker.domain.model.Coin
import com.buildsol.cryptotracker.domain.model.CoinDetails
import com.buildsol.cryptotracker.domain.model.CoinIndexItem
import com.buildsol.cryptotracker.domain.model.MarketChart
import com.buildsol.cryptotracker.domain.model.SearchCoin
import com.buildsol.cryptotracker.domain.repository.CoinRepository
import java.io.IOException

class CoinRepositoryImpl(
    private val coinGeckoApi: CoinGeckoApi,
    private val coinDao: CoinDao,
    private val coinIndex: CoinIndex
) : CoinRepository {

    companion object {
        const val PAGE_SIZE = 50
    }

    override suspend fun getMarket(page: Int): List<Coin> {
        return try {
            val dtoList = coinGeckoApi.getMarketCoins(page = page, perPage = PAGE_SIZE)

            coinDao.insertAll(dtoList.toEntity())

            dtoList.toDomain()
        } catch (e: IOException) {
            val offset = (page - 1) * PAGE_SIZE
            coinDao.getCoinsPage(limit = PAGE_SIZE, offset = offset).toDomain()
        }
    }

    override suspend fun getCoinDetails(coinId: String): CoinDetails {
        return coinGeckoApi.getCoinDetails(coinId).toDomain()
    }

    override suspend fun search(query: String): List<SearchCoin> {
        return coinGeckoApi.searchCoins(query = query).coins.map{it.toDomain()}
    }

    override suspend fun getMarketChart(coinId: String, days: Int): MarketChart {
        return coinGeckoApi.getMarketChart(coinId = coinId,days = days).toDomain()
    }

    override suspend fun loadCoinIndex() {
        if(coinIndex.isLoaded()) return
        val coinIndexList = coinGeckoApi.getCoinList().map{it.toDomain()}
        coinIndex.update(coinIndexList)
    }

    override fun autocomplete(query: String): List<CoinIndexItem> {
        return coinIndex.search(query)
    }
}