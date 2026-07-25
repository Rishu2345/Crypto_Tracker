package com.buildsol.cryptotracker.domain.model

data class MarketChart(
    val prices: List<ChartPoint>,
    val marketCaps: List<ChartPoint>,
    val totalVolumes: List<ChartPoint>
)