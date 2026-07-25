package com.buildsol.cryptotracker.data.remote.dto.details

import kotlinx.serialization.Serializable

@Serializable
data class CoinDetailsDto(
    val id: String,
    val symbol: String,
    val name: String,

    val image: ImageDto,
    val links: LinksDto,
    val market_data: MarketDataDto,

    val hashing_algorithm: String?,
    val genesis_date: String?,
    val categories: List<String>,

    val description: Map<String, String>? = null,
    val localization: Map<String, String>? = null,

    val community_data: CommunityDataDto? = null,
    val developer_data: DeveloperDataDto? = null,
    val public_interest_stats: PublicInterestDto? = null
)