package com.buildsol.cryptotracker.data.remote.api

import com.buildsol.cryptotracker.data.remote.dto.chart.MarketChartDto
import com.buildsol.cryptotracker.data.remote.dto.details.CoinDetailsDto
import com.buildsol.cryptotracker.data.remote.dto.market.CoinMarketDto
import com.buildsol.cryptotracker.data.remote.dto.search.CoinListDto
import com.buildsol.cryptotracker.data.remote.dto.search.SearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinGeckoApi{


    @GET("coins/markets")
    suspend fun getMarketCoins(
        @Query("vs_currency") currency: String = "usd",
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") perPage: Int = 10,
        @Query("page") page: Int,
        @Query("sparkline") sparkline: Boolean = false,
        @Query("price_change_percentage")
        priceChangePercentage: String = "24h"
    ): List<CoinMarketDto>


    @GET("search")
    suspend fun searchCoins(
        @Query("query")
        query: String
    ): SearchResponseDto


    @GET("coins/list")
    suspend fun getCoinList(): List<CoinListDto>

    @GET("coins/{id}/market_chart")
    suspend fun getMarketChart(
        @Path("id") coinId: String,
        @Query("vs_currency") currency: String = "usd",
        @Query("days") days: Int
    ): MarketChartDto


    @GET("coins/{id}")
    suspend fun getCoinDetails(
        @Path("id") coinId: String,
        @Query("localization") localization: Boolean = false,
        @Query("tickers") tickers: Boolean = false,
        @Query("market_data") marketData: Boolean = true,
        @Query("community_data") communityData: Boolean = false,
        @Query("developer_data") developerData: Boolean = false,
        @Query("sparkline") sparkline: Boolean = false
    ): CoinDetailsDto

}

