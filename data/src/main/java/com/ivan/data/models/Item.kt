package com.ivan.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemsListPage(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Item>
)

@Serializable
data class Item(
    val id: Int,
    val title: String,
    val url: String,
    @SerialName("image_url") val imageUrl: String,
    @SerialName("news_site") val newsSite: String,
    val summary: String,
    @SerialName("published_at") val publishedAt: String,
    @SerialName("updated_at") val updatedAt: String,
    val featured: Boolean = false,
    val launches: List<Launch> = emptyList(),
    val events: List<Event> = emptyList()
)

@Serializable
data class Launch(
    @SerialName("launch_id") val launchId: String, // UUID
    val provider: String
)

@Serializable
data class Event(
    @SerialName("event_id") val eventId: Int,
    val provider: String
)
