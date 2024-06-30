package com.challenge.felipeajc.pixabaysearch.domain

import com.challenge.felipeajc.pixabaysearch.data.entities.PixabayImage
import com.challenge.felipeajc.pixabaysearch.data.entities.toImageModel
import org.junit.Assert.assertEquals
import org.junit.Test

class PixabayImageExtensionsTest {

    @Test
    fun tryConvertsCorrectly() {
        val pixabayImage = PixabayImage(
            id = 123,
            likes = 10,
            downloads = 5,
            comments = 2,
            tags = "banana, fruit",
            largeImageURL = "https://example.com/largeimage",
            user = "testUser",
            previewURL = "https://example.com/preview"
        )

        val result = pixabayImage.toImageModel()

        assertEquals(123L, result.imageId)
        assertEquals("testUser", result.userName)
        assertEquals("https://example.com/preview", result.url)
        assertEquals("10", result.likes)
        assertEquals("5", result.downloads)
        assertEquals("2", result.comments)
        assertEquals(listOf("banana", "fruit"), result.tags)
        assertEquals("https://example.com/largeimage", result.largeImageURL)
    }

    @Test
    fun handlesNullValues() {
        val pixabayImage = PixabayImage(
            id = null,
            downloads = null,
            comments = null,
            largeImageURL = null,
            user = null,
            previewURL = null
        )

        val result = pixabayImage.toImageModel()

        assertEquals(-1L, result.imageId)
        assertEquals("", result.userName)
        assertEquals("", result.url)
        assertEquals("null", result.downloads)
        assertEquals("null", result.comments)
        assertEquals(null, result.largeImageURL)
    }
}
