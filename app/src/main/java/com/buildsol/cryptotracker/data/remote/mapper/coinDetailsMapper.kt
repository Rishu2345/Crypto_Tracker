package com.buildsol.cryptotracker.data.remote.mapper

import com.buildsol.cryptotracker.data.remote.dto.details.CoinDetailsDto
import com.buildsol.cryptotracker.domain.model.CoinDetails

fun CoinDetailsDto.toDomain() = CoinDetails(

    id = id,

    symbol = symbol.uppercase(),

    name = name,

    image = image.large,

    description = description?.get("en").orEmpty(),

    hashingAlgorithm = hashing_algorithm,

    categories = categories,

    currentPrice = market_data.current_price["usd"] ?: 0.0,

    marketCap = market_data.market_cap["usd"] ?: 0.0,

    totalVolume = market_data.total_volume["usd"] ?: 0.0,

    high24h = market_data.high_24h["usd"] ?: 0.0,

    low24h = market_data.low_24h["usd"] ?: 0.0,

    priceChange24h = market_data.price_change_24h,

    priceChangePercentage24h =
        market_data.price_change_percentage_24h,

    ath = market_data.ath["usd"] ?: 0.0,

    athDate = market_data.ath_date["usd"].orEmpty(),

    atl = market_data.atl["usd"] ?: 0.0,

    atlDate = market_data.atl_date["usd"].orEmpty(),

    circulatingSupply = market_data.circulating_supply,

    totalSupply = market_data.total_supply,

    maxSupply = market_data.max_supply,

    genesisDate = genesis_date,

    homepage = links.homepage.firstOrNull(),

    lastUpdated = last_updated
)