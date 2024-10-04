package com.ivan.spaceflightnews.screens.main

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.ivan.data.models.Item
import com.ivan.data.state.DataState
import com.ivan.spaceflightnews.ItemDetails
import com.ivan.spaceflightnews.Section
import com.ivan.spaceflightnews.common.ItemType
import com.ivan.spaceflightnews.screens.commonviews.ErrorView
import com.ivan.spaceflightnews.screens.commonviews.LoaderView
import com.ivan.spaceflightnews.screens.utils.GreetingParser
import org.koin.androidx.compose.koinViewModel
import java.util.Calendar

@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = koinViewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val commonDetailsAction = { itemId: Int ->
        navController.navigate(ItemDetails(ItemType.Articles, itemId))
    }
    val userName = viewModel.getUserNameFlow().collectAsState(initial = "")

    val articlesList = viewModel.articlesListLiveData.observeAsState()
    val blogsList = viewModel.blogsListLiveData.observeAsState()
    val reportsList = viewModel.reportsListLiveData.observeAsState()

    Column {
        Header(
            context,
            userName,
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .weight(0.16f)
        )
        articlesList.value?.let {
            ItemsSection(ItemType.Articles, it, Modifier.weight(0.28f), {
                navController.navigate(Section(ItemType.Articles))
            }, commonDetailsAction)
        }
        blogsList.value?.let {
            ItemsSection(ItemType.Blogs, it, Modifier.weight(0.28f), {
                navController.navigate(Section(ItemType.Blogs))
            }, commonDetailsAction)
        }
        reportsList.value?.let {
            ItemsSection(ItemType.Reports, it, Modifier.weight(0.28f), {
                navController.navigate(Section(ItemType.Reports))
            }, commonDetailsAction)
        }
    }
}

@Composable
fun Header(context: Context, userName: State<String>, modifier: Modifier) {
    Text(text = GreetingParser.getGreeting(context, userName.value), modifier = modifier
        .wrapContentWidth()
        .wrapContentHeight(), fontSize = 24.sp, textAlign = TextAlign.Center)
}

@Composable
fun ItemsSection(
    itemType: ItemType,
    dataState: DataState,
    modifier: Modifier,
    goToSectionAction: () -> Unit,
    goToDetailsAction: (itemId: Int) -> Unit
) {
    val rowScrollState = rememberScrollState()
    val title = when(itemType) {
        ItemType.Articles -> stringResource(id = com.ivan.spaceflightnews.R.string.articles)
        ItemType.Blogs -> stringResource(id = com.ivan.spaceflightnews.R.string.blogs)
        ItemType.Reports -> stringResource(id = com.ivan.spaceflightnews.R.string.reports)
    }

    Column(modifier = modifier) {
        Row(
            Modifier
                .height(32.dp)
                .padding(horizontal = 8.dp)) {
            Text(text = title, modifier = Modifier
                .height(32.dp)
                .wrapContentHeight(align = Alignment.CenterVertically))
            Spacer(Modifier.weight(1.0f))
            TextButton(onClick = goToSectionAction, modifier = Modifier.height(32.dp)) {
                Text(text = "See More")
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        when(dataState) {
            is DataState.Loading -> {
                LoaderView(modifier = Modifier.fillMaxSize())
            }
            is DataState.Success<*> -> {
                Row(
                    Modifier
                        .horizontalScroll(rowScrollState)
                        .padding(horizontal = 8.dp)) {
                    (dataState.data as List<Item>).forEachIndexed { index, item ->
                        AsyncImage(
                            model = item.imageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxHeight()
                                .aspectRatio(1.0f)
                                .padding(start = if (index == 0) 0.dp else 8.dp)
                                .clickable { goToDetailsAction(item.id) },
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
            is DataState.Error -> {
                ErrorView(modifier = Modifier.fillMaxSize())
            }
        }
    }
}