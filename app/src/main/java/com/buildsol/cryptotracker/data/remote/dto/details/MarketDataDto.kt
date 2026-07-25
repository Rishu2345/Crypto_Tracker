package com.buildsol.cryptotracker.data.remote.dto.details

import kotlinx.serialization.Serializable

@Serializable
data class MarketDataDto(

    val current_price: Map<String, Double>,

    val market_cap: Map<String, Double>,

    val total_volume: Map<String, Double>,

    val high_24h: Map<String, Double>,

    val low_24h: Map<String, Double>,

    val ath: Map<String, Double>,

    val atl: Map<String, Double>,

    val ath_change_percentage: Map<String, Double>,

    val atl_change_percentage: Map<String, Double>,

    val ath_date: Map<String, String>,

    val atl_date: Map<String, String>,

    val price_change_24h: Double?,

    val price_change_percentage_24h: Double?,

    val circulating_supply: Double?,

    val total_supply: Double?,

    val max_supply: Double?,

    val market_cap_rank: Int,

    val fully_diluted_valuation: Map<String, Double>?,

    val price_change_percentage_7d: Double?,

    val price_change_percentage_14d: Double?,

    val price_change_percentage_30d: Double?,

    val price_change_percentage_60d: Double?,

    val price_change_percentage_200d: Double?,

    val price_change_percentage_1y: Double?
)