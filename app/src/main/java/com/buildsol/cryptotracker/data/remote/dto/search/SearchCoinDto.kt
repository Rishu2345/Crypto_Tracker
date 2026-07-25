package com.buildsol.cryptotracker.data.remote.dto.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchCoinDto(

    val id: String,

    val name: String,

    @SerialName("api_symbol")
    val apiSymbol: String,

    val symbol: String,

    @SerialName("market_cap_rank")
    val marketCapRank: Int?,

    val thumb: String,

    val large: String

)