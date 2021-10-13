package com.challenge.felipeajc.pixabaysearch.data

import com.challenge.felipeajc.pixabaysearch.data.entities.PixabayImage
import kotlinx.coroutines.flow.Flow

interface ImagesRepositoryContract {
    fun searchPixabayImage(query: String): Flow<List<PixabayImage>>
}