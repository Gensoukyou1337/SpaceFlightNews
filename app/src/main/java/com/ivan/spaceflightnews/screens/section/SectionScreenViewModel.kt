package com.ivan.spaceflightnews.screens.section

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ivan.data.common.ItemsRepository
import com.ivan.data.common.OrderingType
import com.ivan.data.models.Item
import com.ivan.data.sites.NewsSitesDataSource
import com.ivan.data.state.DataState
import com.ivan.spaceflightnews.common.ItemType
import com.ivan.spaceflightnews.screens.utils.ItemTypeParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.qualifier.named
import timber.log.Timber

class SectionScreenViewModel(private val newsSitesDataSource: NewsSitesDataSource): ViewModel(), KoinComponent {

    private lateinit var repository: ItemsRepository

    private val _allNewsSitesList: MutableLiveData<List<String>> = MutableLiveData(emptyList())
    val allNewsSitesList: LiveData<List<String>> get() = _allNewsSitesList

    private val _selectedNewsSitesList: MutableLiveData<List<String>> = MutableLiveData(emptyList())
    val selectedNewsSitesList: LiveData<List<String>> get() = _selectedNewsSitesList

    private val _selectedSortMode: MutableLiveData<OrderingType> = MutableLiveData(OrderingType.PublishedAtDescending)
    val selectedSortMode: LiveData<OrderingType> get() = _selectedSortMode

    private val _pagingData: MutableStateFlow<PagingData<Item>> = MutableStateFlow(PagingData.empty())
    val pagingData: StateFlow<PagingData<Item>> get() = _pagingData

    fun initRepository(type: ItemType) {
        val nameToGet = ItemTypeParser.getRepoName(type)
        repository = get(named(nameToGet))
        updatePagingData()
    }

    fun populateNewsSitesList() {
        viewModelScope.launch(Dispatchers.IO) {
            newsSitesDataSource.getNewsSitesForFilter().collect {
                if (it is DataState.Success<*>) {
                    Timber.i("news sites filter ${it.data}")
                    _allNewsSitesList.postValue(it.data as List<String>)
                }
            }
        }
    }

    fun addSelectedNewsSiteFilter(newsSite: String) {
        val previousList = _selectedNewsSitesList.value?.toMutableList() ?: mutableListOf()
        previousList.add(newsSite)
        _selectedNewsSitesList.value = previousList
    }

    fun removeSelectedNewsSiteFilter(newsSite: String) {
        val previousList = _selectedNewsSitesList.value?.toMutableList() ?: mutableListOf()
        previousList.remove(newsSite)
        _selectedNewsSitesList.value = previousList
    }

    fun setSelectedOrderingMode(newOrderingType: OrderingType) {
        _selectedSortMode.value = newOrderingType
    }

    fun updatePagingData() {
        val newsSitesFilters = _selectedNewsSitesList.value ?: emptyList()
        val orderingType = _selectedSortMode.value ?: OrderingType.PublishedAtDescending

        viewModelScope.launch(Dispatchers.IO) {
            repository.getItemsFilteredAndSortedForPager(newsSitesFilters, orderingType).distinctUntilChanged().cachedIn(viewModelScope).collect {
                _pagingData.value = it
            }
        }
    }

}