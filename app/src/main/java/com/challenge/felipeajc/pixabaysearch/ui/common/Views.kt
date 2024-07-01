package com.challenge.felipeajc.pixabaysearch.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.challenge.felipeajc.pixabaysearch.data.entities.PixabayImageModel
import com.challenge.felipeajc.pixabaysearch.ui.imagesearch.IconTextUI

@Composable
fun ImageUI(
    modifier: Modifier = Modifier,
    imageModel: PixabayImageModel,
    showExtraData: Boolean = false,
) {

    Box(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Black
                    ),
                )
            )
            .fillMaxWidth()
            .height(220.dp)
            .padding(horizontal = 8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(bottom = 12.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconTextUI(icon = Icons.Outlined.AccountCircle, imageModel.userName)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TagsList(imageModel.tags)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ExtraInfosUI(showExtraData, imageModel)
            }
        }
    }
}

@Composable
private fun TagsList(tags: List<String>) {
    LazyRow(
        modifier = Modifier,
        horizontalArrangement = Arrangement.Start,
        content = {
            items(tags) { TagUI(tagName = it) }
        }
    )
}

@Composable
private fun ExtraInfosUI(
    showExtra: Boolean,
    imageModel: PixabayImageModel,
) {
    if (showExtra) {
        IconTextUI(icon = Icons.Outlined.ThumbUp, imageModel.likes)
        IconTextUI(icon = Icons.Outlined.Menu, imageModel.comments)
    }
}

@Composable
fun TagUI(
    tagName: String,
) {
    Text(
        text = tagName,
        style = MaterialTheme.typography.titleSmall.copy(color = Color.White)
    )
}
