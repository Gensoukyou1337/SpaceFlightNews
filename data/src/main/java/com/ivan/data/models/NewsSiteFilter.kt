package com.ivan.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class NewsSiteFilter(
    val version: String,
    @SerialName("news_sites") val newsSites: List<String>
)
