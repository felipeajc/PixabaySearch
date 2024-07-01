package com.challenge.felipeajc.pixabaysearch.domain

import com.challenge.felipeajc.pixabaysearch.data.ImagesRepositoryContract
import com.challenge.felipeajc.pixabaysearch.data.entities.PixabayImage
import com.challenge.felipeajc.pixabaysearch.data.entities.toImageModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class SearchPixabayImageUsecaseTest {

    @Mock
    private lateinit var mockRepository: ImagesRepositoryContract

    private lateinit var usecase: SearchPixabayImageUsecase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        usecase = SearchPixabayImageUsecase(mockRepository)
    }

    @Test
    fun `invoke with non-empty list returns Success state`() = runTest {
        val query = "fruits"
        val mockImages = listOf(PixabayImage(id = 1, previewURL = "url1", tags = "fruits"))
        val expectedImages = mockImages.map { it.toImageModel() }

        `when`(mockRepository.searchPixabayImage(query)).thenReturn(flow {
            emit(mockImages)
        })

        usecase(query).collect { result ->
            assertEquals(SearchState.Success(expectedImages), result)
        }
    }

    @Test
    fun `invoke with empty list returns Empty state`() = runTest {
        val query = "nonexistent"

        `when`(mockRepository.searchPixabayImage(query)).thenReturn(flow {
            emit(emptyList<PixabayImage>())
        })

        usecase(query).collect { result ->
            assertEquals(SearchState.Loading, result)
        }
    }

    @Test
    fun `invoke with error returns Error state`() = runTest {
        val query = "error"

        val mockError = RuntimeException("Repository error")
        `when`(mockRepository.searchPixabayImage(query)).thenThrow(mockError)

        usecase(query).collect { result ->
            assertTrue(result is SearchState.Error)
            assertEquals(mockError, (result as SearchState.Error).throwable)
        }
    }
}
