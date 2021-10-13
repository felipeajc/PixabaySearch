package com.challenge.felipeajc.pixabaysearch.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WifiOff
import com.challenge.felipeajc.pixabaysearch.R
import java.net.UnknownHostException

fun Throwable.toUiModel(): ErrorUiModel = when (this) {
    is UnknownHostException -> ErrorUiModel(
        errorMessageId = R.string.no_internet_connection,
        errorIconId = Icons.Outlined.WifiOff
    )
    else -> ErrorUiModel()
}