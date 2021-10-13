package com.challenge.felipeajc.pixabaysearch.ui.imagesearch

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.challenge.felipeajc.pixabaysearch.R
import com.challenge.felipeajc.pixabaysearch.data.entities.PixabayImageModel
import com.challenge.felipeajc.pixabaysearch.domain.SearchState
import com.challenge.felipeajc.pixabaysearch.domain.SearchState.*
import com.challenge.felipeajc.pixabaysearch.ui.common.ImageUI
import com.challenge.felipeajc.pixabaysearch.ui.common.isPortrait
import com.challenge.felipeajc.pixabaysearch.ui.common.toUiModel


@Preview
@Composable
fun PreviewSearch() {
    SearchScreenContent(searchState = Empty)
}

@Composable
fun SearchScreen(viewModel: SearchImagesViewModel, onNavigateClicked: (Long) -> Unit = { }) {
    val viewState by viewModel.searchViewState.collectAsState()
    val query by viewModel.searchQueryState.collectAsState()

    SearchScreenContent(
        onImageClick = onNavigateClicked,
        searchText = query,
        searchState = viewState,
        onSearchChange = { viewModel.onSearchChange(it) },
        onSearchClicked = { viewModel.onSearchClicked() }
    )
}

@Composable
fun OpenImageDetailsConfirmationDialog(
    showDialog: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    if (showDialog) {
        AlertDialog(
            modifier = Modifier.fillMaxWidth(),
            title = { Text(stringResource(R.string.do_you_want_to_open_image_details)) },
            onDismissRequest = onDismiss,
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.End,

                    ) {
                    TextButton(onClick = onDismiss) {
                        Text(text = stringResource(R.string.no))
                    }
                    TextButton(onClick = onConfirm) {
                        Text(text = stringResource(R.string.yes))
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchScreenContent(
    searchText: String = "",
    onSearchChange: (String) -> Unit = {},
    onSearchClicked: () -> Unit = {},
    searchState: SearchState = Empty,
    onImageClick: (Long) -> Unit = {},
) {
    var showDialog by remember { mutableStateOf(false) }
    var imageId by remember { mutableStateOf(-1L) }

    Box(
        modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column {
            Row {
                SearchTopbar(
                    searchText = searchText,
                    onSearchChange = onSearchChange,
                    onSearchClicked = onSearchClicked,
                    isLoading = searchState is Loading,
                )
            }
            Row {
                when (searchState) {
                    is Success -> ImageListView(searchState.pixabayImages) {
                        imageId = it
                        showDialog = true
                    }
                    is Empty -> EmptyListMessageView()
                    is Error -> ErrorView(searchState.throwable)
                }
            }
        }
    }

    OpenImageDetailsConfirmationDialog(showDialog = showDialog, onConfirm = {
        showDialog = false
        onImageClick(imageId)
    }) {
        showDialog = false
    }
}

@Composable
fun SearchTopbar(
    modifier: Modifier = Modifier,
    searchText: String = "",
    onSearchChange: (String) -> Unit = {},
    onSearchClicked: () -> Unit = {},
    isLoading: Boolean,
) {
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp),
        value = searchText,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.surface,
            cursorColor = MaterialTheme.colors.onSurface,
            disabledLabelColor = MaterialTheme.colors.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        onValueChange = {
            onSearchChange(it)
        },
        shape = RoundedCornerShape(8.dp),
        singleLine = true,
        trailingIcon = { LoadingCOntentImage(isLoading, onSearchClicked) },
        keyboardActions = KeyboardActions(onDone = { onSearchClicked() })
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ImageListView(
    pixabayImages: List<PixabayImageModel>,
    onImageClicked: (Long) -> Unit,
) {
    val currentConfig = LocalConfiguration.current
    LazyVerticalGrid(
        modifier = Modifier,
        cells = GridCells.Fixed(if (currentConfig.isPortrait()) 2 else 5),
        content = {
            itemsIndexed(pixabayImages) { _, item ->
                ImageItemUI(
                    modifier = Modifier.padding(8.dp),
                    pixabayImage = item,
                    onImageClicked = { onImageClicked(it) }
                )
            }
        }
    )
}

@Composable
fun PlaceHolderView(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    message: String,
    iconTint: Color = MaterialTheme.colors.secondaryVariant,
    textColor: Color = Color.Unspecified,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.size(120.dp),
                imageVector = icon,
                tint = iconTint.copy(alpha = 0.5f),
                contentDescription = ""
            )
            Text(
                text = message,
                style = MaterialTheme.typography.subtitle1,
                textAlign = TextAlign.Center,
                color = textColor.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
private fun EmptyListMessageView() {
    PlaceHolderView(
        modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp),
        icon = Icons.Outlined.Error,
        stringResource(R.string.empty_view_message)
    )
}

@Composable
fun ErrorView(
    throwable: Throwable,
) {
    val uiModel = throwable.toUiModel()
    PlaceHolderView(
        icon = uiModel.errorIconId,
        message = stringResource(id = uiModel.errorMessageId),
        iconTint = MaterialTheme.colors.error
    )
}

@Composable
private fun LoadingCOntentImage(isLoading: Boolean, onSearchClicked: () -> Unit) {
    if (isLoading) {
        CircularProgressIndicator(
            modifier = Modifier.size(24.dp),
            strokeWidth = 2.dp
        )
    } else {
        IconButton(onClick = { onSearchClicked() }) {
            Icon(
                imageVector = Icons.Outlined.Search,
                tint = MaterialTheme.colors.onSurface,
                contentDescription = ""
            )
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ImageItemUI(
    modifier: Modifier = Modifier,
    pixabayImage: PixabayImageModel,
    showExtraInfos: Boolean = false,
    onImageClicked: (Long) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onImageClicked(pixabayImage.imageId) },
        contentAlignment = Alignment.BottomStart
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = rememberImagePainter(pixabayImage.url),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )

        ImageUI(pixabayImage = pixabayImage, showExtraInfos = showExtraInfos)
    }
}

@Composable
fun IconTextUI(icon: ImageVector, text: String) {
    Row(
        modifier = Modifier.wrapContentSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "",
            tint = Color.White
        )
        Text(
            text = text,
            style = MaterialTheme.typography.caption.copy(color = Color.White),
        )
    }
}