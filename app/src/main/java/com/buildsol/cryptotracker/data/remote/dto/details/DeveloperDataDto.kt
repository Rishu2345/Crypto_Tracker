package com.buildsol.cryptotracker.data.remote.dto.details

import kotlinx.serialization.Serializable

@Serializable
data class DeveloperDataDto(
    val forks: Int,
    val stars: Int,
    val subscribers: Int,
    val total_issues: Int,
    val closed_issues: Int,
    val pull_requests_merged: Int,
    val pull_request_contributors: Int
)