package com.buildsol.cryptotracker.data.remote.mapper

import com.buildsol.cryptotracker.data.mapper.toDomain
import com.buildsol.cryptotracker.data.mapper.toEntity
import com.buildsol.cryptotracker.data.remote.dto.market.CoinMarketDto
import org.junit.Assert.assertEquals
import org.junit.Test


class CoinMarketMapperTest {

    private fun sampleDto(
        priceChangePercentage24h: Double? = -1.83121
    ) = CoinMarketDto(
        id = "bitcoin",
        symbol = "btc",
        name = "Bitcoin",
        image = "https://coin-images.coingecko.com/coins/images/1/large/bitcoin.png",
        currentPrice = 64089.0,
        marketCap = 1285680566357.0,
        marketCapRank = 1,
        fullyDilutedValuation = 1285680566357.0,
        totalVolume = 24968643773.0,
        high24h = 65700.0,
        low24h = 63713.0,
        priceChange24h = -1195.49,
        priceChangePercentage24h = priceChangePercentage24h,
        marketCapChange24h = -24004942603.0,
        marketCapChangePercentage24h = -1.83288,
        circulatingSupply = 20060928.0,
        totalSupply = 20060928.0,
        maxSupply = 21000000.0,
        lastUpdated = "2026-07-25T05:21:29.669Z",
        ath = 73738.0,
        athChangePercentage = -13.09,
        athDate = "2025-03-14T07:10:36.635Z",
        atl = 67.81,
        atlChangePercentage = 94398.55,
        atlDate = "2013-07-06T00:00:00.000Z",
        roi = null,
        priceChangePercentage24hInCurrency = priceChangePercentage24h
    )

    @Test
    fun `toDomain maps every field correctly`() {
        val dto = sampleDto()

        val domain = dto.toDomain()

        assertEquals("bitcoin", domain.id)
        assertEquals("BTC", domain.symbol)
        assertEquals("Bitcoin", domain.name)
        assertEquals(64089.0, domain.price, 0.0)
        assertEquals(1285680566357.0, domain.marketCap, 0.0)
        assertEquals(1, domain.rank)
        assertEquals(-1.83121, domain.priceChangePercentage24h!!, 0.0001)
    }

    @Test
    fun `toDomain preserves a null price change percentage instead of coercing to zero`() {
        val dto = sampleDto(priceChangePercentage24h = null)

        val domain = dto.toDomain()

        assertEquals(null, domain.priceChangePercentage24h)
    }

    @Test
    fun `toEntity uppercases symbol the same way toDomain does`() {
        val dto = sampleDto()

        val entity = dto.toEntity()

        assertEquals("BTC", entity.symbol)
    }
}