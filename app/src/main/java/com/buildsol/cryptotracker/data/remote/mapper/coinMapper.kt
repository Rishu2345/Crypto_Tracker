package com.buildsol.cryptotracker.data.remote.mapper

import com.buildsol.cryptotracker.data.local.entity.CoinEntity
import com.buildsol.cryptotracker.data.remote.dto.market.CoinMarketDto
import com.buildsol.cryptotracker.domain.model.Coin

fun CoinMarketDto.toDomain() = Coin(
    id = id,
    symbol = symbol.uppercase(),
    name = name,
    image = image,
    price = currentPrice,
    marketCap = marketCap,
    rank = marketCapRank,
    priceChangePercentage24h = priceChangePercentage24h
)

fun CoinMarketDto.toEntity() = CoinEntity(
    id = id,
    symbol = symbol.uppercase(),
    name = name,
    image = image,
    currentPrice = currentPrice,
    marketCap = marketCap,
    marketCapRank = marketCapRank,
    priceChange24h = priceChange24h,
    priceChangePercentage24h = priceChangePercentage24h,
    lastUpdated = lastUpdated
)

fun CoinEntity.toDomain() = Coin(
    id = id,
    symbol = symbol,
    name = name,
    image = image,
    price = currentPrice,
    marketCap = marketCap,
    rank = marketCapRank,
    priceChangePercentage24h = priceChangePercentage24h
)

fun List<CoinMarketDto>.toDomain(): List<Coin> = map { it.toDomain() }
fun List<CoinMarketDto>.toEntity(): List<CoinEntity> = map { it.toEntity() }
fun List<CoinEntity>.toDomain(): List<Coin> = map { it.toDomain() }