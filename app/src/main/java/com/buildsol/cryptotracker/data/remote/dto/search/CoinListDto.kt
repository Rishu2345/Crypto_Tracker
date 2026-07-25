// app/src/main/java/com/buildsol/cryptotracker/data/remote/dto/search/CoinListDto.kt

package com.buildsol.cryptotracker.data.remote.dto.search

import kotlinx.serialization.Serializable

@Serializable
data class CoinListDto(
    val id: String,
    val symbol: String,
    val name: String
)