package com.buildsol.cryptotracker.data.mapper

import com.buildsol.cryptotracker.data.remote.dto.chart.MarketChartDto
import com.buildsol.cryptotracker.domain.model.ChartPoint
import com.buildsol.cryptotracker.domain.model.MarketChart

fun MarketChartDto.toDomain() = MarketChart(
    prices = prices.map { it.toChartPoint() },
    marketCaps = market_caps.map { it.toChartPoint() },
    totalVolumes = total_volumes.map { it.toChartPoint() }
)

fun List<Double>.toChartPoint() = ChartPoint(
    timestamp = this[0].toLong(),
    value = this[1]
)
