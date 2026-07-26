package com.buildsol.cryptotracker.data.repository

import com.buildsol.cryptotracker.data.local.dao.CoinDao
import com.buildsol.cryptotracker.data.local.database.CoinIndex
import com.buildsol.cryptotracker.data.local.entity.CoinEntity
import com.buildsol.cryptotracker.data.remote.api.CoinGeckoApi
import com.buildsol.cryptotracker.data.remote.dto.market.CoinMarketDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

class CoinRepositoryImplTest {
    private lateinit var api: CoinGeckoApi
    private lateinit var dao: CoinDao
    private lateinit var coinIndex: CoinIndex
    private lateinit var repository: CoinRepositoryImpl

    @Before
    fun setup() {
        api = mockk()
        dao = mockk()
        coinIndex = mockk()
        repository = CoinRepositoryImpl(api, dao,coinIndex)
    }

    private fun dto(id: String, rank: Int) = CoinMarketDto(
        id = id,
        symbol = id.take(3),
        name = id,
        image = "https://example.com/$id.png",
        currentPrice = 100.0,
        marketCap = 1_000_000.0,
        marketCapRank = rank,
        fullyDilutedValuation = null,
        totalVolume = 5000.0,
        high24h = 110.0,
        low24h = 90.0,
        priceChange24h = 1.0,
        priceChangePercentage24h = 1.0,
        marketCapChange24h = null,
        marketCapChangePercentage24h = null,
        circulatingSupply = 1000.0,
        totalSupply = null,
        maxSupply = null,
        lastUpdated = "2026-07-25T05:21:29.669Z",

        ath = 120.0,
        athChangePercentage = -16.67,
        athDate = "2026-06-01T00:00:00.000Z",

        atl = 10.0,
        atlChangePercentage = 900.0,
        atlDate = "2020-01-01T00:00:00.000Z",

        roi = null,

        priceChangePercentage24hInCurrency = 1.0
    )

    @Test
    fun `getMarket returns network data and writes it through to Room on success`() = runTest {
        val networkResult = listOf(dto("bitcoin", 1), dto("ethereum", 2))
        coEvery { api.getMarketCoins(page = 1, perPage = any()) } returns networkResult
        coEvery { dao.insertAll(any()) } returns Unit

        val result = repository.getMarket(page = 1)

        assertEquals(2, result.size)
        assertEquals("bitcoin", result[0].id)
        assertEquals("BIT", result[0].symbol)

        coVerify(exactly = 1) { dao.insertAll(any()) }
    }

    @Test
    fun `getMarket falls back to cached Room page when network is unreachable`() = runTest {
        coEvery { api.getMarketCoins(page = 2, perPage = any()) } throws IOException("no connection")

        val cachedPage = listOf(
            CoinEntity(
                id = "cardano",
                symbol = "ADA",
                name = "Cardano",
                image = "https://example.com/cardano.png",
                currentPrice = 0.45,
                marketCap = 6_000_000_000.0,
                marketCapRank = 20,
                priceChange24h = -0.01,
                priceChangePercentage24h = -2.3,
                lastUpdated = "2026-07-25T05:21:29.669Z"
            )
        )
        coEvery { dao.getCoinsPage(limit = any(), offset = any()) } returns cachedPage

        val result = repository.getMarket(page = 2)

        assertEquals(1, result.size)
        assertEquals("cardano", result[0].id)

        coVerify(exactly = 0) { dao.insertAll(any()) }
    }
}