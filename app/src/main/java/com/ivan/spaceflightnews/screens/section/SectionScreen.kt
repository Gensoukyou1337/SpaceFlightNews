package com.ivan.spaceflightnews.screens.section

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.ivan.data.common.OrderingType
import com.ivan.spaceflightnews.ItemDetails
import com.ivan.spaceflightnews.Search
import com.ivan.spaceflightnews.Section
import com.ivan.spaceflightnews.common.ItemType
import com.ivan.spaceflightnews.screens.commonviews.ErrorView
import com.ivan.spaceflightnews.screens.commonviews.ItemLargeView
import com.ivan.spaceflightnews.screens.commonviews.LoaderView
import org.koin.androidx.compose.koinViewModel

@Composable
fun SectionScreen(
    navController: NavController,
    data: Section,
    viewModel: SectionScreenViewModel = koinViewModel()
) {
    val itemPagingData = viewModel.pagingData.collectAsLazyPagingItems()
    val newsSites = viewModel.allNewsSitesList.observeAsState(initial = emptyList())
    val selectedNewsSites = viewModel.selectedNewsSitesList.observeAsState(initial = emptyList())
    val displayNewsSiteFilterDialog = remember {
        mutableStateOf(false)
    }
    val displaySortDialog = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(true) {
        viewModel.initRepository(data.itemType)
        viewModel.populateNewsSitesList()
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(8.dp)) {
        Text(
            text = when(data.itemType) {
                ItemType.Articles -> stringResource(id = com.ivan.spaceflightnews.R.string.articles)
                ItemType.Blogs -> stringResource(id = com.ivan.spaceflightnews.R.string.blogs)
                ItemType.Reports -> stringResource(id = com.ivan.spaceflightnews.R.string.reports)
            },
            modifier = Modifier
                .height(32.dp)
                .wrapContentHeight()
                .fillMaxWidth()
        )
        Row {
            OutlinedButton(onClick = {
                navController.navigate(Search(data.itemType))
            }) {
                Text("Search")
            }
            Spacer(Modifier.weight(1.0f))
            OutlinedButton(onClick = { displayNewsSiteFilterDialog.value = true }) {
                Text("Filter")
            }
            OutlinedButton(onClick = { displaySortDialog.value = true }) {
                Text("Sort")
            }
        }
        LazyColumn(Modifier.weight(1f)) {
            items(itemPagingData.itemCount) { index ->
                itemPagingData[index]?.let {
                    ItemLargeView(it, index) {
                        navController.navigate(
                            ItemDetails(
                                itemType = data.itemType,
                                itemId = itemPagingData[index]!!.id
                            )
                        )
                    }
                }
            }
            itemPagingData.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        item {LoaderView(modifier = Modifier
                            .fillMaxWidth()
                            .height(96.dp))}
                    }
                    loadState.refresh is LoadState.Error -> {
                        item {
                            ErrorView(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(96.dp)
                            )
                        }
                    }
                    loadState.append is LoadState.Loading -> {
                        item {LoaderView(modifier = Modifier
                            .fillMaxWidth()
                            .height(96.dp))}
                    }
                    loadState.append is LoadState.Error -> {
                        item {
                            ErrorView(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(96.dp)
                            )
                        }
                    }
                }
            }
        }
    }
    
    if (displayNewsSiteFilterDialog.value) {
        NewsSiteFilterDialog(
            viewModel,
            newsSites.value,
            selectedNewsSites, {
                displayNewsSiteFilterDialog.value = false
            }, {
                displayNewsSiteFilterDialog.value = false
                viewModel.updatePagingData()
            }
        )
    }

    if (displaySortDialog.value) {
        SortFilterDialog(
            viewModel,
            {
                displaySortDialog.value = false
            }, {
                displaySortDialog.value = false
                viewModel.updatePagingData()
            }
        )
    }
}

@Composable
private fun NewsSiteFilterDialog(
    viewModel: SectionScreenViewModel,
    newsSites: List<String>,
    selectedNewsSites: State<List<String>>,
    onDismissRequest: () -> Unit,
    onConfirmRequest: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.fillMaxSize()) {
                Text("Filter By Checked News Sites")
                LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                    items(newsSites.size) {index ->
                        Row(Modifier.fillMaxWidth()) {
                            Checkbox(
                                checked = selectedNewsSites.value.contains(newsSites[index]),
                                onCheckedChange = {
                                    if (it) {
                                        viewModel.addSelectedNewsSiteFilter(newsSites[index])
                                    } else {
                                        viewModel.removeSelectedNewsSiteFilter(newsSites[index])
                                    }
                                })
                            Text(newsSites[index], fontSize = 10.sp, modifier = Modifier
                                .fillMaxHeight()
                                .wrapContentHeight())
                        }
                    }
                }
                Button(
                    onClick = onConfirmRequest
                ) {
                    Text(text = "Apply Filter")
                }
            }
        }
    }
}

@Composable
private fun SortFilterDialog(
    viewModel: SectionScreenViewModel,
    onDismissRequest: () -> Unit,
    onConfirmRequest: () -> Unit
) {
    val orderingMode = viewModel.selectedSortMode.observeAsState()
    Dialog(onDismissRequest = onDismissRequest) {
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.fillMaxSize()) {
                Text("Sort By:")
                Row(Modifier.fillMaxWidth()) {
                    RadioButton(
                        selected = orderingMode.value == OrderingType.PublishedAtDescending,
                        onClick = { viewModel.setSelectedOrderingMode(OrderingType.PublishedAtDescending) }
                    )
                    Text("Publish Date - Latest to Earliest")
                }
                Row(Modifier.fillMaxWidth()) {
                    RadioButton(
                        selected = orderingMode.value == OrderingType.PublishedAtAscending,
                        onClick = { viewModel.setSelectedOrderingMode(OrderingType.PublishedAtAscending) }
                    )
                    Text("Publish Date - Earliest to Latest")
                }
                Row(Modifier.fillMaxWidth()) {
                    RadioButton(
                        selected = orderingMode.value == OrderingType.UpdatedAtDescending,
                        onClick = { viewModel.setSelectedOrderingMode(OrderingType.UpdatedAtDescending) }
                    )
                    Text("Last Update Date - Latest to Earliest")
                }
                Row(Modifier.fillMaxWidth()) {
                    RadioButton(
                        selected = orderingMode.value == OrderingType.UpdatedAtAscending,
                        onClick = { viewModel.setSelectedOrderingMode(OrderingType.UpdatedAtAscending) }
                    )
                    Text("Last Update Date - Earliest to Latest")
                }
                Button(
                    onClick = onConfirmRequest
                ) {
                    Text(text = "Apply Ordering")
                }
            }
        }
    }
}