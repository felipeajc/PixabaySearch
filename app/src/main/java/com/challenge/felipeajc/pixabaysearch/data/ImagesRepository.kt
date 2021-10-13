package com.challenge.felipeajc.pixabaysearch.data

import com.challenge.felipeajc.pixabaysearch.data.entities.PixabayImage
import com.challenge.felipeajc.pixabaysearch.data.local.LocalPixabayImageStoreContract
import com.challenge.felipeajc.pixabaysearch.data.remote.RemotePixabayContract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class ImagesRepository @Inject constructor(
    private val remote: RemotePixabayContract,
    private val localImage: LocalPixabayImageStoreContract,
) : ImagesRepositoryContract {

    override fun searchPixabayImage(query: String): Flow<List<PixabayImage>> =
        flow {
            try {
                val listImages = remote.search(query).images
                if (listImages != null && listImages.isNotEmpty()) {

                    localImage.deleteAllImages()

                    localImage.saveImages(listImages)
                }
            } catch (e: Exception) {
                Timber.e("Exception: $e")
            }
            emit(localImage.getAllImages())
        }
}