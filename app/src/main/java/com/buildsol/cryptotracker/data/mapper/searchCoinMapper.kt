package com.buildsol.cryptotracker.data.mapper

import com.buildsol.cryptotracker.data.remote.dto.search.SearchCoinDto
import com.buildsol.cryptotracker.domain.model.SearchCoin

fun SearchCoinDto.toDomain() = SearchCoin(
    id = id,
    name = name,
    symbol = symbol,
    image = large,
    marketCapRank = marketCapRank
)