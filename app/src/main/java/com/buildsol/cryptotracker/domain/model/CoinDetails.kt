package com.buildsol.cryptotracker.domain.model

data class CoinDetails(

    val id: String,

    val symbol: String,

    val name: String,

    val image: String,

    val description: String,

    val hashingAlgorithm: String?,

    val categories: List<String>,

    val currentPrice: Double,

    val marketCap: Double,

    val totalVolume: Double,

    val high24h: Double,

    val low24h: Double,

    val priceChange24h: Double?,

    val priceChangePercentage24h: Double?,

    val ath: Double,

    val athDate: String,

    val atl: Double,

    val atlDate: String,

    val circulatingSupply: Double?,

    val totalSupply: Double?,

    val maxSupply: Double?,

    val genesisDate: String?,

    val homepage: String?,

    val lastUpdated:String?
)