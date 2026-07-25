package com.buildsol.cryptotracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "coins")
data class CoinEntity(
    @PrimaryKey val id: String,
    val symbol: String,
    val name: String,
    val image: String,
    val currentPrice: Double,
    val marketCap: Double,
    val marketCapRank: Int,
    val priceChange24h: Double?,
    val priceChangePercentage24h: Double?,
    val lastUpdated: String
)