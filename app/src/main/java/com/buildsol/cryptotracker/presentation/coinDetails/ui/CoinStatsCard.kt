package com.buildsol.cryptotracker.presentation.coinDetails.ui

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import com.buildsol.cryptotracker.domain.model.CoinDetails
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
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StatRow(
            leftLabel = "Market Cap",
            leftValue = formatMarketCap(details.marketCap),
            rightLabel = "24h High",
            rightValue = formatPrice(details.high24h)
        )
        StatRow(
            leftLabel = "24h Low",
            leftValue = formatPrice(details.low24h),
            rightLabel = "Last Updated",
            rightValue = formatLastUpdated(details.lastUpdated)
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
    Row(modifier = Modifier.fillMaxWidth()) {
        StatCell(label = leftLabel, value = leftValue, modifier = Modifier.weight(1f))
        StatCell(label = rightLabel, value = rightValue, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun StatCell(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
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