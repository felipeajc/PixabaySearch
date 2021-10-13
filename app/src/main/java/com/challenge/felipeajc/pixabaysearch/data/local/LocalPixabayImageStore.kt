package com.challenge.felipeajc.pixabaysearch.data.local

import com.challenge.felipeajc.pixabaysearch.data.entities.PixabayImage
import javax.inject.Inject

class LocalPixabayImageStore @Inject constructor(
    private val pixabayImageDao: PixabayImageDao,
) : LocalPixabayImageStoreContract {

    override suspend fun getAllImages(): List<PixabayImage> {
        return pixabayImageDao.getAllImages()
    }

    override suspend fun saveImages(listImages: List<PixabayImage>) {
        return pixabayImageDao.insertAllImages(listImages)
    }

    override suspend fun deleteAllImages() {
        pixabayImageDao.deleteAllImages()
    }
}