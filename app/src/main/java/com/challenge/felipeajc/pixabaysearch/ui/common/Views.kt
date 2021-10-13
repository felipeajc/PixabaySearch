package com.challenge.felipeajc.pixabaysearch.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.challenge.felipeajc.pixabaysearch.R
import com.challenge.felipeajc.pixabaysearch.data.entities.PixabayImageModel
import com.challenge.felipeajc.pixabaysearch.ui.imagesearch.IconTextUI

@Composable
fun ImageUI(
    modifier: Modifier = Modifier,
    pixabayImage: PixabayImageModel,
    showExtraInfos: Boolean = false,
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
                IconTextUI(icon = Icons.Outlined.AccountCircle, pixabayImage.userName)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TagsList(pixabayImage.tags)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ExtraInfosUI(showExtraInfos, pixabayImage)
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
    showExtraInfos: Boolean,
    pixabayImage: PixabayImageModel,
) {
    if (showExtraInfos) {
        IconTextUI(icon = Icons.Outlined.ThumbUp, pixabayImage.likes)
        IconTextUI(icon = Icons.Outlined.Comment, pixabayImage.comments)
    }
}

@Composable
fun TagUI(
    tagName: String,
) {
    Text(
        text = stringResource(R.string.tag_prefix) + tagName,
        style = MaterialTheme.typography.caption.copy(color = Color.White)
    )
}
