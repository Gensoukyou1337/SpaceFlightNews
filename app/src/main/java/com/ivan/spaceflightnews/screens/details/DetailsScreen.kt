package com.ivan.spaceflightnews.screens.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ivan.spaceflightnews.ItemDetails
import com.ivan.spaceflightnews.screens.commonviews.LoaderView
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailsScreen(data: ItemDetails, viewModel: DetailsViewModel = koinViewModel()) {
    val itemDetails = viewModel.itemDetails.observeAsState()

    LaunchedEffect(key1 = "ViewModel Init") {
        viewModel.initRepository(data.itemType)
        viewModel.fetchItemDetails(data.itemId)
    }

    itemDetails.value?.let {
        Column(Modifier.fillMaxSize().padding(8.dp)) {
            AsyncImage(
                model = it.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(128.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = it.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                fontSize = 32.sp,
                lineHeight = 36.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = it.publishedAt,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Summary",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .wrapContentHeight(),
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it.summary.split(". ").first())
        }
    } ?: run {
        LoaderView(modifier = Modifier.fillMaxSize())
    }
}