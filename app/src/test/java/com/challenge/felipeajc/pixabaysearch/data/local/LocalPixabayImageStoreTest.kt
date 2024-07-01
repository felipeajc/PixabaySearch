package com.challenge.felipeajc.pixabaysearch.data.local

import com.challenge.felipeajc.pixabaysearch.data.entities.PixabayImage
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class LocalPixabayImageStoreTest {

    @Mock
    private lateinit var mockPixabayImageDao: PixabayImageDao

    private lateinit var localPixabayImageStore: LocalPixabayImageStoreContract

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        localPixabayImageStore = LocalPixabayImageStore(mockPixabayImageDao)
    }

    @Test
    fun testGetAllImages() = runBlocking {
        // Mock data
        val mockImages = listOf(
            PixabayImage(id = 1, previewURL = "url1"),
            PixabayImage(id = 2, previewURL = "url2")
        )

        `when`(mockPixabayImageDao.getAllImages()).thenReturn(mockImages)

        val result = localPixabayImageStore.getAllImages()

        assert(result == mockImages)
    }

    @Test
    fun testSaveImages(): Unit = runBlocking {
        val mockImages = listOf(
            PixabayImage(id = 1, previewURL = "url1"),
            PixabayImage(id = 2, previewURL = "url2")
        )

        localPixabayImageStore.saveImages(mockImages)
        verify(mockPixabayImageDao).insertAllImages(mockImages)
    }

    @Test
    fun testDeleteAllImages(): Unit = runBlocking {
        localPixabayImageStore.deleteAllImages()
        verify(mockPixabayImageDao).deleteAllImages()
    }
}
