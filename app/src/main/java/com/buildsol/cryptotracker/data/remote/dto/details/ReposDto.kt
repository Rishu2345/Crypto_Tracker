package com.buildsol.cryptotracker.data.remote.dto.details

import kotlinx.serialization.Serializable

@Serializable
data class ReposDto(
    val github: List<String>,
    val bitbucket: List<String>
)