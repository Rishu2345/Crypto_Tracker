package com.buildsol.cryptotracker.data.remote.dto.details

import kotlinx.serialization.Serializable

@Serializable
data class CommunityDataDto(
    val facebook_likes: Int?,
    val twitter_followers: Int?,
    val reddit_average_posts_48h: Double?,
    val reddit_average_comments_48h: Double?,
    val reddit_subscribers: Int?
)