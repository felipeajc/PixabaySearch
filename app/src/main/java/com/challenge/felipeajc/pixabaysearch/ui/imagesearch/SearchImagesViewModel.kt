package com.challenge.felipeajc.pixabaysearch.ui.imagesearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.felipeajc.pixabaysearch.data.entities.PixabayImageModel
import com.challenge.felipeajc.pixabaysearch.domain.SearchPixabayImageUsecase
import com.challenge.felipeajc.pixabaysearch.domain.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SearchImagesViewModel @Inject constructor(
    val searchPixabayImageUsecase: SearchPixabayImageUsecase,
) : ViewModel() {

    private var _searchViewState = MutableStateFlow<SearchState>(SearchState.Empty)
    val searchViewState: StateFlow<SearchState> = _searchViewState

    private var _searchQueryState = MutableStateFlow("fruits")
    val searchQueryState: StateFlow<String> = _searchQueryState

    init {
        onSearchClicked()
    }

    fun searchImages(query: String) {
        searchPixabayImageUsecase(query = query)
            .onEach { _searchViewState.value = it }
            .launchIn(viewModelScope)
    }

    fun onSearchChange(searchText: String) {
        _searchQueryState.value = searchText
    }

    fun findImage(id: Long): PixabayImageModel? =
        (_searchViewState.value as? SearchState.Success)?.pixabayImages?.find { it.imageId == id }

    fun onSearchClicked() {
        searchImages(_searchQueryState.value)
    }
}