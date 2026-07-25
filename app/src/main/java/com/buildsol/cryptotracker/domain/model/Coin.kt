package com.buildsol.cryptotracker.domain.model

data class Coin(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String,
    val price: Double,
    val marketCap: Double,
    val rank: Int,
    val priceChangePercentage24h: Double?
)