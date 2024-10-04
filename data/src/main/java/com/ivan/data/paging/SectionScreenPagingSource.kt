package com.ivan.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ivan.data.common.ItemsDataSource
import com.ivan.data.models.Item


class SectionScreenPagingSource(
    private val itemsDataSource: ItemsDataSource,
    private val newsSiteFilter: List<String>,
    private val orderingParameter: String?
): PagingSource<Int, Item>() {
    override fun getRefreshKey(state: PagingState<Int, Item>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
        val currentPage = params.key ?: 0
        val itemsPerPage = 20
        val offset = currentPage * itemsPerPage

        return try {
            val items = itemsDataSource.getItemsFilteredAndSorted(
                newsSiteFilter.joinToString(separator = ","),
                orderingParameter,
                offset,
                itemsPerPage,
            )

            LoadResult.Page(
                data = items,
                prevKey = if (currentPage == 0) null else currentPage - 1,
                nextKey = if (items.isEmpty()) null else currentPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}