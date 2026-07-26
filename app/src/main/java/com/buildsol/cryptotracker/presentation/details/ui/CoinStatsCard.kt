package com.buildsol.cryptotracker.presentation.details.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.buildsol.cryptotracker.domain.model.CoinDetails
import com.buildsol.cryptotracker.ui.theme.CryptoTrackerTheme
import com.buildsol.cryptotracker.ui.theme.PriceStatValue
import com.buildsol.cryptotracker.utils.formatLastUpdated
import com.buildsol.cryptotracker.utils.formatMarketCap
import com.buildsol.cryptotracker.utils.formatPrice

@Composable
fun CoinStatsCard(details: CoinDetails, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp,MaterialTheme.colorScheme.outline,RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "MARKET OVERVIEW",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        StatRow(
            leftLabel = "Current Price",
            leftValue = formatPrice(details.currentPrice),
            rightLabel = "Market Cap",
            rightValue = formatMarketCap(details.marketCap)
        )

        StatRow(
            leftLabel = "24h High",
            leftValue = formatPrice(details.high24h),
            rightLabel = "24h Low",
            rightValue = formatPrice(details.low24h)
        )

        StatRow(
            leftLabel = "24h Change",
            leftValue = details.priceChange24h?.let(::formatPrice) ?: "-",
            rightLabel = "24h %",
            rightValue = details.priceChangePercentage24h
                ?.let { "%.2f%%".format(it) } ?: "-"
        )

        StatRow(
            leftLabel = "Volume",
            leftValue = formatMarketCap(details.totalVolume),
            rightLabel = "Circulating",
            rightValue = details.circulatingSupply?.let(::formatMarketCap) ?: "-"
        )

        StatRow(
            leftLabel = "Last Updated",
            leftValue = formatLastUpdated(details.lastUpdated),
            rightLabel = "Max Supply",
            rightValue = details.maxSupply?.let(::formatMarketCap) ?: "Unlimited"
        )
    }
}

@Composable
private fun StatRow(
    leftLabel: String,
    leftValue: String,
    rightLabel: String,
    rightValue: String
) {
    Row(modifier = Modifier.fillMaxWidth(),Arrangement.SpaceBetween) {
        StatCell(label = leftLabel, value = leftValue, modifier = Modifier.weight(1f))
        StatCell(label = rightLabel, value = rightValue, modifier = Modifier.weight(1f))
    }
}

@Composable

private fun StatCell(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, Arrangement.spacedBy(8.dp)) {
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = PriceStatValue,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Start
        )
    }
}

@Preview(
    showBackground = true,
)
@Composable
private fun CoinStatsCardPreview() {
    CryptoTrackerTheme(darkTheme = true) {
        CoinStatsCard(
            details = CoinDetails(
                id = "bitcoin",
                symbol = "BTC",
                name = "Bitcoin",
                image = "https://assets.coingecko.com/coins/images/1/large/bitcoin.png",
                description = "Bitcoin is the first decentralized cryptocurrency.",
                hashingAlgorithm = "SHA-256",
                categories = listOf("Cryptocurrency", "Layer 1", "Store of Value"),
                currentPrice = 118543.72,
                marketCap = 2_350_000_000_000.0,
                totalVolume = 42_500_000_000.0,
                high24h = 119_850.12,
                low24h = 116_200.45,
                priceChange24h = 1450.28,
                priceChangePercentage24h = 1.24,
                ath = 123_091.61,
                athDate = "2026-07-14T10:15:00.000Z",
                atl = 67.81,
                atlDate = "2013-07-06T00:00:00.000Z",
                circulatingSupply = 19_900_000.0,
                totalSupply = 21_000_000.0,
                maxSupply = 21_000_000.0,
                genesisDate = "2009-01-03",
                homepage = "https://bitcoin.org",
                lastUpdated = "2026-07-26T10:30:00.000Z"
            )
        )
    }
}