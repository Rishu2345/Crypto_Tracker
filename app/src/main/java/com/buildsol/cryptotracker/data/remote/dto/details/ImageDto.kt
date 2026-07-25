package com.buildsol.cryptotracker.data.remote.dto.details

import kotlinx.serialization.Serializable

@Serializable
data class ImageDto(
    val thumb: String,
    val small: String,
    val large: String
)