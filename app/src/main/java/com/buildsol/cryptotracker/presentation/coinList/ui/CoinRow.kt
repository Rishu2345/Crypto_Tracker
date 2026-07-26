package com.buildsol.cryptotracker.presentation.coinList.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.buildsol.cryptotracker.domain.model.Coin
import com.buildsol.cryptotracker.ui.component.PriceChangeChip
import com.buildsol.cryptotracker.ui.theme.PriceMedium

@Composable
fun CoinRow(
    coin: Coin,
    onClick: (Coin) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onClick(coin) }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                model = coin.image,
                contentDescription = coin.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
            )
            Column {
                Text(
                    text = coin.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = coin.symbol,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = formatPrice(coin.price),
                style = PriceMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.padding(top = 2.dp))
            PriceChangeChip(percentage = coin.priceChangePercentage24h)
        }
    }
}

// Simple, dependency-free currency formatting. Swap for NumberFormat/
// java.text if you need locale-aware grouping, but this covers the
// assignment's USD-only requirement without pulling in extra formatting
// machinery under deadline pressure.
private fun formatPrice(price: Double): String {
    return when {
        price >= 1 -> "$" + "%,.2f".format(price)
        price > 0 -> "$" + "%.6f".format(price).trimEnd('0').trimEnd('.')
        else -> "$0.00"
    }
}