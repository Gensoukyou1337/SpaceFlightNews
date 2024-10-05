package com.ivan.spaceflightnews.screens.commonviews

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ivan.data.models.Item
import com.ivan.spaceflightnews.ItemDetails

@Composable
fun ItemLargeView(item: Item, index : Int, onClick: (() -> Unit)?) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(16.0f / 9)
        .padding(top = if (index == 0) 0.dp else 8.dp)
        .clickable {
            onClick?.invoke()
        }
    ) {
        AsyncImage(
            model = item.imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color(0.0f, 0.0f, 0.0f, 0.2f))
        ) {
            Text(text = item.title ?: "", modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter), fontSize = 24.sp)
            if (item.launches.isNotEmpty()) {
                Text(
                    text = "Launches: ${item.launches.first().provider}",
                    modifier = Modifier.align(Alignment.BottomStart)
                )
            }
            if (item.events.isNotEmpty()) {
                Text(
                    text = "Events: ${item.events.first().provider}",
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
        }
    }
}