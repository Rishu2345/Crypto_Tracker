package com.buildsol.cryptotracker.data.remote.mapper

import com.buildsol.cryptotracker.data.remote.dto.search.CoinListDto
import com.buildsol.cryptotracker.domain.model.CoinListItem

fun CoinListDto.toDomain() = CoinListItem(
    id = id,
    symbol = symbol,
    name = name
)