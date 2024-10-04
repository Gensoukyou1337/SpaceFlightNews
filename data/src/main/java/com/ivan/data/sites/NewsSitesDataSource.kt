package com.ivan.data.sites

import com.ivan.data.state.DataState
import kotlinx.coroutines.flow.Flow

interface NewsSitesDataSource {
    fun getNewsSitesForFilter(): Flow<DataState>
}