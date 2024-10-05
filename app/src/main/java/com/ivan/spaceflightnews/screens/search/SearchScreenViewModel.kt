package com.ivan.spaceflightnews.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ivan.data.common.ItemsRepository
import com.ivan.data.models.Item
import com.ivan.spaceflightnews.common.ItemType
import com.ivan.spaceflightnews.screens.utils.ItemTypeParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.qualifier.named
import timber.log.Timber

class SearchScreenViewModel(
    private val recentSearchTermsStorage: RecentSearchTermsStorage
): ViewModel(), KoinComponent {

    companion object {
        private const val RECENTLY_SEARCHED_ITEMS_COUNT_LIMIT = 5
    }

    private lateinit var repository: ItemsRepository

    private val _searchResultsPagingData: MutableStateFlow<PagingData<Item>> = MutableStateFlow(PagingData.empty())
    val searchResultsPagingData: StateFlow<PagingData<Item>> get() = _searchResultsPagingData

    private val _recentlySearchedItemsData: MutableStateFlow<List<Item>> = MutableStateFlow(emptyList())
    val recentlySearchedItemsData: StateFlow<List<Item>> get() = _recentlySearchedItemsData

    private var currentRecentlySearchedItemsList: MutableList<Item> = mutableListOf()

    fun initRepository(type: ItemType) {
        val nameToGet = ItemTypeParser.getRepoName(type)
        repository = get(named(nameToGet))
    }

    // currentRecentlySearchedItemsList could probably be moved into RecentSearchItemsStorage...
    fun getRecentlySearchedItems() {
        viewModelScope.launch(Dispatchers.IO) {
            recentSearchTermsStorage.getRecentSearchTerms().collect {
                val searchedItems = if (it.isNotEmpty()) {
                    Json.decodeFromString<RecentlySearchedItemsData>(it).searchedItems
                } else {
                    emptyList()
                }
                currentRecentlySearchedItemsList = searchedItems.toMutableList()
                _recentlySearchedItemsData.value = searchedItems
            }
        }
    }
    fun addToRecentlySearchedItems(clickedSearchItem: Item) {
        if (currentRecentlySearchedItemsList.contains(clickedSearchItem)) {
            return
        }

        currentRecentlySearchedItemsList.add(clickedSearchItem)
        if (currentRecentlySearchedItemsList.size > RECENTLY_SEARCHED_ITEMS_COUNT_LIMIT) {
            currentRecentlySearchedItemsList.removeAt(0)
        }

        viewModelScope.launch(Dispatchers.IO) {
            recentSearchTermsStorage.saveRecentSearchTerms(
                Json.encodeToString(RecentlySearchedItemsData(currentRecentlySearchedItemsList.toList()))
            )
        }
    }

    fun startSearch(searchTerm: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getSearchedItemsForPager(searchTerm).distinctUntilChanged().cachedIn(viewModelScope).collect {
                _searchResultsPagingData.value = it
            }
        }
    }

}