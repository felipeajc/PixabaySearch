package com.challenge.felipeajc.pixabaysearch.ui.imagedetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import com.challenge.felipeajc.pixabaysearch.data.entities.PixabayImageModel
import com.challenge.felipeajc.pixabaysearch.ui.common.ImageUI
import com.google.accompanist.coil.rememberCoilPainter

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ImageDetailsScreen(pixabayImage: PixabayImageModel) {
    var scale by remember { mutableFloatStateOf(1f) }
    val rotationState = remember { mutableFloatStateOf(1f) }

    Box(
        modifier = Modifier
            .background(color = Color.Transparent)
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, _, zoom, rotation ->
                    scale *= zoom
                    rotationState.value += rotation
                }
            },
    ) {
        Image(
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer(
                    scaleX = maxOf(.5f, minOf(3f, scale)),
                    scaleY = maxOf(.5f, minOf(3f, scale))
                ),
            painter = rememberCoilPainter(request = pixabayImage.largeImageURL),
            contentDescription = ""
        )
        ImageUI(
            modifier = Modifier.align(Alignment.BottomCenter),
            pixabayImage = pixabayImage,
            showExtraInfos = true
        )
    }
}