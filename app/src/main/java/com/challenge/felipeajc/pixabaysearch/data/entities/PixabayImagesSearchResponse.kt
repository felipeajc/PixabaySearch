package com.challenge.felipeajc.pixabaysearch.data.entities

import com.squareup.moshi.Json

data class PixabayImagesSearchResponse(
    @field:Json(name = "totalHits")
    val totalHits: Int? = 0,
    @field:Json(name = "total")
    val total: Int? = 0,
    @field:Json(name = "hits")
    val images: List<PixabayImage>? = listOf()
)