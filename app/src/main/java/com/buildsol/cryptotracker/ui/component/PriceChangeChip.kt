package com.buildsol.cryptotracker.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.buildsol.cryptotracker.ui.theme.PricePercentage
import com.buildsol.cryptotracker.ui.theme.priceColors
import kotlin.math.abs


@Composable
fun PriceChangeChip(percentage: Double?, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.priceColors

    if (percentage == null) {
        Row(
            modifier = modifier
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "—",
                style = PricePercentage,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    val isPositive = percentage >= 0
    val containerColor = if (isPositive) colors.positiveContainer else colors.negativeContainer
    val contentColor = if (isPositive) colors.positive else colors.negative

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(containerColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = if (isPositive) Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(12.dp)
        )
        Text(
            text = "${"%.2f".format(abs(percentage))}%",
            style = PricePercentage,
            color = contentColor
        )
    }
}