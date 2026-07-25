// app/src/main/java/com/buildsol/cryptotracker/data/remote/dto/search/SearchResponseDto.kt

package com.buildsol.cryptotracker.data.remote.dto.search

import kotlinx.serialization.Serializable

@Serializable
data class SearchResponseDto(
    val coins: List<SearchCoinDto>
)