package com.ivan.spaceflightnews.screens.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivan.data.common.ItemsRepository
import com.ivan.data.models.Item
import com.ivan.data.state.DataState
import com.ivan.spaceflightnews.common.ItemType
import com.ivan.spaceflightnews.screens.utils.ItemTypeParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.qualifier.named

class DetailsViewModel : ViewModel(), KoinComponent {

    private lateinit var repository: ItemsRepository
    private val _itemDetails: MutableLiveData<Item?> = MutableLiveData(null)
    val itemDetails: LiveData<Item?> get() = _itemDetails

    fun initRepository(type: ItemType) {
        val nameToGet = ItemTypeParser.getRepoName(type)
        repository = get(named(nameToGet))
    }

    fun fetchItemDetails(itemId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getSpecificItem(itemId).collect {
                if (it is DataState.Success<*>) {
                    _itemDetails.postValue(it.data as Item)
                }
            }
        }
    }

}