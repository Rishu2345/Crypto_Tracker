package com.buildsol.cryptotracker.data.remote.dto.details

import kotlinx.serialization.Serializable

@Serializable
data class PublicInterestDto(
    val alexa_rank: Int?,
    val bing_matches: Int?
)