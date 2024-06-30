package com.challenge.felipeajc.pixabaysearch.ui.imagesearch

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.challenge.felipeajc.pixabaysearch.R
import com.challenge.felipeajc.pixabaysearch.data.entities.PixabayImageModel
import com.challenge.felipeajc.pixabaysearch.domain.SearchState
import com.challenge.felipeajc.pixabaysearch.domain.SearchState.Empty
import com.challenge.felipeajc.pixabaysearch.domain.SearchState.Error
import com.challenge.felipeajc.pixabaysearch.domain.SearchState.Loading
import com.challenge.felipeajc.pixabaysearch.domain.SearchState.Success
import com.challenge.felipeajc.pixabaysearch.ui.common.ImageUI
import com.challenge.felipeajc.pixabaysearch.ui.common.isPortrait
import com.challenge.felipeajc.pixabaysearch.ui.common.toUiModel
import com.google.accompanist.coil.rememberCoilPainter

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
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false,
            )
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.do_you_want_to_open_image_details),
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = onDismiss,
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Text(text = stringResource(R.string.no))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(
                            onClick = onConfirm,
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Text(text = stringResource(R.string.yes))
                        }
                    }
                }
            }
        }
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
                when(searchState) {
                    is Success -> ImageListView(searchState.pixabayImages) {
                        imageId = it
                        showDialog = true
                    }

                    is Empty -> EmptyListMessageView()
                    is Error -> ErrorView(searchState.throwable)
                    Loading ->  EmptyListMessageView()
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
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp),
        value = searchText,
        onValueChange = {
            onSearchChange(it)
        },
        label = { Text("Search") },
        trailingIcon = { LoadingContentImage(isLoading, onSearchClicked) },
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
        columns = GridCells.Fixed(if (currentConfig.isPortrait()) 2 else 5),
        content = {
            itemsIndexed(pixabayImages) { _, item ->
                Surface(
                    modifier = Modifier
                        .padding(8.dp)
                        .aspectRatio(1f)
                        .clickable { onImageClicked(item.imageId) },
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White
                ) {
                    ImageItemUI(
                        modifier = Modifier.padding(8.dp),
                        pixabayImage = item,
                        onImageClicked = { onImageClicked(it) }
                    )
                }
            }
        }
    )
}

@Composable
fun PlaceHolderView(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    message: String,
    iconTint: Color = MaterialTheme.colorScheme.surfaceVariant,
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
                style = MaterialTheme.typography.titleSmall,
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
        icon = Icons.Outlined.Close,
        message = stringResource(R.string.empty_view_message)
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
        iconTint = MaterialTheme.colorScheme.error
    )
}

@Composable
private fun LoadingContentImage(isLoading: Boolean, onSearchClicked: () -> Unit) {
    if (isLoading) {
        CircularProgressIndicator(
            modifier = Modifier.size(24.dp),
            strokeWidth = 2.dp
        )
    } else {
        IconButton(onClick = { onSearchClicked() }) {
            Icon(
                imageVector = Icons.Outlined.Search,
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = ""
            )
        }
    }
}

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
            painter = rememberCoilPainter(pixabayImage.url),
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
            style = MaterialTheme.typography.titleMedium.copy(color = Color.White),
        )
    }
}
