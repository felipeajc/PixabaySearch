package com.challenge.felipeajc.pixabaysearch.domain

import com.challenge.felipeajc.pixabaysearch.data.ImagesRepositoryContract
import com.challenge.felipeajc.pixabaysearch.data.entities.PixabayImageModel
import com.challenge.felipeajc.pixabaysearch.data.entities.toImageModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class SearchPixabayImageUsecase @Inject constructor(
    private val imagesRepository: ImagesRepositoryContract,
) {
    operator fun invoke(query: String): Flow<SearchState> {
        return imagesRepository.searchPixabayImage(query)
            .map { list -> list.map { it.toImageModel() } }
            .map { if (it.isEmpty()) SearchState.Empty else SearchState.Success(it) }
            .onStart { emit(SearchState.Loading) }
            .catch { emit(SearchState.Error(it)) }
    }
}

sealed class SearchState {
    object Empty : SearchState()
    class Success(val pixabayImages: List<PixabayImageModel>) : SearchState()
    class Error(val throwable: Throwable) : SearchState()
    object Loading : SearchState()
}