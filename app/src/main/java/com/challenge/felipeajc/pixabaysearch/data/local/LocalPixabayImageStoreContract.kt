package com.challenge.felipeajc.pixabaysearch.data.local

import com.challenge.felipeajc.pixabaysearch.data.entities.PixabayImage


interface LocalPixabayImageStoreContract {

    suspend fun saveImages(listImages: List<PixabayImage>)
    suspend fun getAllImages(): List<PixabayImage>
    suspend fun deleteAllImages()
}