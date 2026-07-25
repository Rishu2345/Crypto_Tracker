package com.buildsol.cryptotracker.data.remote.dto.details

import kotlinx.serialization.Serializable

@Serializable
data class LinksDto(
    val homepage: List<String>,
    val blockchain_site: List<String>,
    val official_forum_url: List<String>,
    val subreddit_url: String?,
    val repos_url: ReposDto,
    val twitter_screen_name: String?,
    val facebook_username: String?
)