package com.challenge.felipeajc.pixabaysearch.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Error
import androidx.compose.ui.graphics.vector.ImageVector
import com.challenge.felipeajc.pixabaysearch.R

data class ErrorUiModel(
    val errorMessageId: Int = R.string.error_message,
    val errorIconId: ImageVector = Icons.Outlined.Error,
)