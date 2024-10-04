package com.ivan.data.common

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ivan.data.models.Item
import com.ivan.data.paging.SearchScreenPagingSource
import com.ivan.data.paging.SectionScreenPagingSource
import com.ivan.data.state.DataState
import kotlinx.coroutines.flow.Flow

class ItemsRepositoryImpl(private val dataSource: ItemsDataSource): ItemsRepository {
    override fun getItemsForTopSection(): Flow<DataState> = dataSource.getItemsTopSection()
    override fun getSpecificItem(itemId: Int): Flow<DataState> = dataSource.getSpecificItem(itemId)

    override fun getUnfilteredItemsForPager(): Flow<PagingData<Item>> =
        Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 1),
            pagingSourceFactory = { SectionScreenPagingSource(dataSource, emptyList(), null) }
        ).flow

    override fun getSearchedItemsForPager(searchTerm: String): Flow<PagingData<Item>> =
        Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 1),
            pagingSourceFactory = { SearchScreenPagingSource(dataSource, searchTerm) }
        ).flow

    override fun getItemsFilteredAndSortedForPager(
        newsSiteFilters: List<String>,
        orderingType: OrderingType?
    ): Flow<PagingData<Item>> =
        Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 1),
            pagingSourceFactory = { SectionScreenPagingSource(dataSource, newsSiteFilters, orderingType?.queryValue) }
        ).flow
}