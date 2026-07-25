package com.buildsol.cryptotracker.data.remote.dto.chart

import kotlinx.serialization.Serializable

@Serializable
data class MarketChartDto(
    val prices: List<List<Double>>,
    val market_caps: List<List<Double>>,
    val total_volumes: List<List<Double>>
)