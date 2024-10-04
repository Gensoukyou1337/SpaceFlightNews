package com.ivan.spaceflightnews.screens.search

import com.ivan.data.models.Item
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecentlySearchedItemsData(
    @SerialName("searched_items") val searchedItems: List<Item>
)
