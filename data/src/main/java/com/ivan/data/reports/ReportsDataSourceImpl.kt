package com.ivan.data.reports

import com.ivan.data.common.ItemsDataSource
import com.ivan.data.models.Item
import com.ivan.data.state.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ReportsDataSourceImpl(private val reportsService: ReportsService) : ItemsDataSource {

    override fun getItemsTopSection(): Flow<DataState> = flow {
        emit(DataState.Success(reportsService.getReports().results))
    }
    override fun getSpecificItem(itemId: Int): Flow<DataState> = flow {
        emit(DataState.Success(reportsService.getSpecificReport(itemId)))
    }

    override suspend fun getItemsForPager(offset: Int, numOfItems: Int): List<Item> {
        return reportsService.getReports(offset = offset, limit = numOfItems).results
    }

    override suspend fun getItemsFilteredAndSorted(
        newsSite: String,
        ordering: String?,
        offset: Int,
        numOfItems: Int
    ): List<Item> {
        return reportsService.getReports(newsSiteFilter = newsSite, offset = offset, limit = numOfItems).results
    }

    override suspend fun getSearchedItems(searchTerm: String, offset: Int, numOfItems: Int): List<Item> {
        return reportsService.getReports(searchTerm = searchTerm, offset = offset, limit = numOfItems).results
    }
}