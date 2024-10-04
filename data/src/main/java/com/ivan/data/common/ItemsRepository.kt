package com.ivan.data.common

import androidx.paging.PagingData
import com.ivan.data.models.Item
import com.ivan.data.state.DataState
import kotlinx.coroutines.flow.Flow

interface ItemsRepository {
    fun getItemsForTopSection(): Flow<DataState>
    fun getSpecificItem(itemId: Int): Flow<DataState>
    fun getUnfilteredItemsForPager(): Flow<PagingData<Item>>

    fun getItemsFilteredAndSortedForPager(
        newsSiteFilters: List<String>,
        orderingType: OrderingType?
    ): Flow<PagingData<Item>>

    fun getSearchedItemsForPager(searchTerm: String): Flow<PagingData<Item>>
}