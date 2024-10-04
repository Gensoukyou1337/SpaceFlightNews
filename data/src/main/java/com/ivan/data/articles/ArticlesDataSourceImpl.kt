package com.ivan.data.articles

import com.ivan.data.common.ItemsDataSource
import com.ivan.data.models.Item
import com.ivan.data.state.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ArticlesDataSourceImpl(private val articlesService: ArticlesService) : ItemsDataSource {

    override fun getItemsTopSection(): Flow<DataState> = flow {
        emit(DataState.Loading)
        try {
            emit(DataState.Success(articlesService.getArticles().results))
        } catch(e: Exception) {
            emit(DataState.Error(e))
        }
    }

    override fun getSpecificItem(itemId: Int): Flow<DataState> = flow {
        emit(DataState.Loading)
        try {
            emit(DataState.Success(articlesService.getSpecificArticle(itemId)))
        } catch(e: Exception) {
            emit(DataState.Error(e))
        }
    }

    override suspend fun getItemsForPager(offset: Int, numOfItems: Int): List<Item> {
        return articlesService.getArticles(offset = offset, limit = numOfItems).results
    }

    override suspend fun getItemsFilteredAndSorted(
        newsSite: String,
        ordering: String?,
        offset: Int,
        numOfItems: Int
    ): List<Item> {
        return articlesService.getArticles(newsSiteFilter = newsSite, ordering = ordering, offset = offset, limit = numOfItems).results
    }

    override suspend fun getSearchedItems(searchTerm: String, offset: Int, numOfItems: Int): List<Item> {
        return articlesService.getArticles(searchTerm = searchTerm, offset = offset, limit = numOfItems).results
    }
}