package com.ivan.spaceflightnews.screens.commonviews

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ErrorView(modifier: Modifier) {
    Box(modifier = modifier) {
        Text(
            text = "Something has gone wrong with the server, please try again later",
            modifier = Modifier.fillMaxSize().wrapContentHeight().wrapContentWidth()
        )
    }
}