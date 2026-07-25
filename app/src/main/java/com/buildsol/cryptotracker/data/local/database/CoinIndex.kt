package com.buildsol.cryptotracker.data.local.database

import com.buildsol.cryptotracker.domain.model.CoinIndexItem


class CoinIndex {
    private var coins: List<CoinIndexItem> = emptyList()

    fun update(items: List<CoinIndexItem>) {
        coins = items
    }
    fun isLoaded() = coins.isNotEmpty()

    fun search(query: String): List<CoinIndexItem> {

        if (query.isBlank()) return emptyList()

        val q = query.lowercase()

        return coins.filter {

            it.name.lowercase().contains(q) ||
                    it.symbol.lowercase().contains(q)

        }.take(20)
    }
}