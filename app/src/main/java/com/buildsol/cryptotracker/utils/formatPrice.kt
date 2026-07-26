package com.buildsol.cryptotracker.utils

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun formatPrice(price: Double?): String {
    if (price == null) return "—"
    return when {
        price >= 1 -> "$" + "%,.2f".format(price)
        price > 0 -> "$" + "%.6f".format(price).trimEnd('0').trimEnd('.')
        else -> "$0.00"
    }
}


fun formatMarketCap(value: Double?): String {
    if (value == null) return "—"
    val absValue = kotlin.math.abs(value)
    return when {
        absValue >= 1_000_000_000_000.0 -> "$" + "%.2f".format(value / 1_000_000_000_000.0) + "T"
        absValue >= 1_000_000_000.0 -> "$" + "%.2f".format(value / 1_000_000_000.0) + "B"
        absValue >= 1_000_000.0 -> "$" + "%.2f".format(value / 1_000_000.0) + "M"
        else -> "$" + "%,.0f".format(value)
    }
}

private val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
    timeZone = TimeZone.getTimeZone("UTC")
}
private val displayFormatter = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())

fun formatLastUpdated(isoTimestamp: String?): String {
    if (isoTimestamp.isNullOrBlank()) return "—"
    return try {
        val date = isoFormatter.parse(isoTimestamp) ?: return "—"
        displayFormatter.format(date)
    } catch (e: Exception) {
        "—"
    }
}