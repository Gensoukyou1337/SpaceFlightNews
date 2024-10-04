package com.ivan.data.common

import com.ivan.data.models.Item
import com.ivan.data.state.DataState
import kotlinx.coroutines.flow.Flow

interface ItemsDataSource {
    fun getItemsTopSection(): Flow<DataState>
    fun getSpecificItem(itemId: Int): Flow<DataState>

    suspend fun getItemsForPager(offset: Int, numOfItems: Int): List<Item>

    suspend fun getSearchedItems(searchTerm: String, offset: Int, numOfItems: Int): List<Item>

    suspend fun getItemsFilteredAndSorted(
        newsSite: String,
        ordering: String?,
        offset: Int,
        numOfItems: Int
    ): List<Item>
}