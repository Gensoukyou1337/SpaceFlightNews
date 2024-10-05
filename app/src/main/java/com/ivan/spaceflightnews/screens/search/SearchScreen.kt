package com.ivan.spaceflightnews.screens.search

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.ivan.spaceflightnews.ItemDetails
import com.ivan.spaceflightnews.Search
import com.ivan.spaceflightnews.screens.commonviews.ItemLargeView
import com.ivan.spaceflightnews.screens.commonviews.LoaderView
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
fun SearchScreen(
    navController: NavController,
    data: Search,
    viewModel: SearchScreenViewModel = koinViewModel()
) {
    var searchTerm by remember {
        mutableStateOf("")
    }
    var hasStartedSearching by remember {
        mutableStateOf(false)
    }
    val localContext = LocalContext.current
    val itemPagingData = viewModel.searchResultsPagingData.collectAsLazyPagingItems()
    val recentlySearchedItems = viewModel.recentlySearchedItemsData.collectAsState()

    LaunchedEffect(true) {
        viewModel.initRepository(data.itemType)
        viewModel.getRecentlySearchedItems()
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Text("Search", modifier = Modifier
            .height(32.dp)
            .wrapContentHeight()
        )
        Row {
            TextField(
                value = searchTerm,
                onValueChange = { searchTerm = it},
                modifier = Modifier
                    .weight(1f),
                label = { Text("Search Term") }
            )
            Button(
                modifier = Modifier
                    .height(32.dp)
                    .align(Alignment.CenterVertically),
                onClick = {
                    hasStartedSearching = true
                    if (searchTerm.isEmpty()) {
                        Toast
                            .makeText(
                                localContext,
                                "Search Term cannot be empty, so type something",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    } else {
                        viewModel.startSearch(searchTerm)
                    }
                }
            ) {
                Text(text = "Search")
            }
        }
        if (!hasStartedSearching) {
            Column(Modifier.weight(1f)) {
                Text(text = "Recently Searched Items", modifier = Modifier
                    .height(32.dp)
                    .wrapContentHeight(), fontSize = 16.sp)
                Column(Modifier.weight(1f)) {
                    recentlySearchedItems.value.forEachIndexed { index, item ->
                        ItemLargeView(item = item, index = index) {
                            navController.navigate(
                                ItemDetails(
                                    data.itemType,
                                    item.id
                                )
                            )
                        }
                    }
                }
            }
        } else {
            Column(Modifier.weight(1f)) {
                Text(text = "Search Results", modifier = Modifier
                    .height(32.dp)
                    .wrapContentHeight(), fontSize = 16.sp)
                LazyColumn(Modifier.weight(1f)) {
                    items(itemPagingData.itemCount) { index ->
                        itemPagingData[index]?.let {
                            ItemLargeView(item = it, index = index) {
                                viewModel.addToRecentlySearchedItems(it)
                                navController.navigate(
                                    ItemDetails(
                                        data.itemType,
                                        it.id
                                    )
                                )
                            }
                        }
                    }
                    itemPagingData.apply {
                        when {
                            loadState.refresh is LoadState.Loading -> {
                                item { LoaderView(modifier = Modifier
                                    .fillMaxWidth()
                                    .height(96.dp)) }
                            }
                            loadState.append is LoadState.Loading -> {
                                item { LoaderView(modifier = Modifier
                                    .fillMaxWidth()
                                    .height(96.dp)) }
                            }
                        }
                    }
                }
            }
        }
    }
}