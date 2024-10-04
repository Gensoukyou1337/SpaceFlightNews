package com.ivan.data.sites

import com.ivan.data.state.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NewsSitesDataSourceImpl(private val newsSitesService: NewsSitesService): NewsSitesDataSource {
    override fun getNewsSitesForFilter(): Flow<DataState> = flow {
        emit(DataState.Success<List<String>>(newsSitesService.getNewsSites().newsSites))
    }
}