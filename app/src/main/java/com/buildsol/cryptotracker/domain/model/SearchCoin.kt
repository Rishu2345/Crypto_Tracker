package com.buildsol.cryptotracker.domain.model

data class SearchCoin(
    val id: String,
    val name: String,
    val symbol: String,
    val image: String,
    val marketCapRank: Int?
)