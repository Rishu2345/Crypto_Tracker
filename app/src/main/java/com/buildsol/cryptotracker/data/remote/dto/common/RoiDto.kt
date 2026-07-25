package com.buildsol.cryptotracker.data.remote.dto.common

import kotlinx.serialization.Serializable

@Serializable
data class RoiDto(

    val times: Double,

    val currency: String,

    val percentage: Double

)