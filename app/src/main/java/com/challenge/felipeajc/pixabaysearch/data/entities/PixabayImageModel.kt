package com.challenge.felipeajc.pixabaysearch.data.entities

data class PixabayImageModel(
    val imageId: Long = -1,
    val userName: String,
    val url: String,
    val likes: String,
    val downloads: String,
    val comments: String,
    val tags: List<String>,
    val largeImageURL: String?
)