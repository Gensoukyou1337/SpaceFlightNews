package com.ivan.spaceflightnews.screens.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivan.data.common.ItemsDataSource
import com.ivan.data.state.DataState
import com.ivan.spaceflightnews.common.LoginCore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val articlesDataSource: ItemsDataSource,
    private val blogsDataSource: ItemsDataSource,
    private val reportsDataSource: ItemsDataSource,
    private val loginCore: LoginCore
) : ViewModel() {

    private val _articlesListLiveData: MutableLiveData<DataState> = MutableLiveData(DataState.Loading)
    val articlesListLiveData: LiveData<DataState> get() = _articlesListLiveData

    private val _blogsListLiveData: MutableLiveData<DataState> = MutableLiveData(DataState.Loading)
    val blogsListLiveData: LiveData<DataState> get() = _blogsListLiveData

    private val _reportsListLiveData: MutableLiveData<DataState> = MutableLiveData(DataState.Loading)
    val reportsListLiveData: LiveData<DataState> get() = _reportsListLiveData

    init {
        getArticlesTest()
    }
    fun getArticlesTest() {
        viewModelScope.launch(Dispatchers.IO) {
            articlesDataSource.getItemsTopSection().collect {
                _articlesListLiveData.postValue(it)
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            blogsDataSource.getItemsTopSection().collect {
                _blogsListLiveData.postValue(it)
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            reportsDataSource.getItemsTopSection().collect {
                _reportsListLiveData.postValue(it)
            }
        }
    }

    fun getUserNameFlow(): Flow<String> = loginCore.getUserNameFlow()
}