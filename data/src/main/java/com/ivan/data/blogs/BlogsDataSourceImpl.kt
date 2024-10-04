package com.ivan.data.blogs

import com.ivan.data.common.ItemsDataSource
import com.ivan.data.models.Item
import com.ivan.data.state.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BlogsDataSourceImpl(private val blogsService: BlogsService) : ItemsDataSource {

    override fun getItemsTopSection(): Flow<DataState> = flow {
        emit(DataState.Success(blogsService.getBlogs().results))
    }
    override fun getSpecificItem(itemId: Int): Flow<DataState> = flow {
        emit(DataState.Success(blogsService.getSpecificBlog(itemId)))
    }
    override suspend fun getItemsForPager(offset: Int, numOfItems: Int): List<Item> {
        return blogsService.getBlogs(offset = offset, limit = numOfItems).results
    }

    override suspend fun getItemsFilteredAndSorted(
        newsSite: String,
        ordering: String?,
        offset: Int,
        numOfItems: Int
    ): List<Item> {
        return blogsService.getBlogs(newsSiteFilter = newsSite, ordering = ordering, offset = offset, limit = numOfItems).results
    }

    override suspend fun getSearchedItems(searchTerm: String, offset: Int, numOfItems: Int): List<Item> {
        return blogsService.getBlogs(searchTerm = searchTerm, offset = offset, limit = numOfItems).results
    }
}