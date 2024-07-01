package com.challenge.felipeajc.pixabaysearch.imagesearch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.challenge.felipeajc.pixabaysearch.data.entities.PixabayImageModel
import com.challenge.felipeajc.pixabaysearch.domain.SearchPixabayImageUsecase
import com.challenge.felipeajc.pixabaysearch.domain.SearchState
import com.challenge.felipeajc.pixabaysearch.ui.imagesearch.SearchImagesViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchImagesViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var mockUsecase: SearchPixabayImageUsecase
    private lateinit var viewModel: SearchImagesViewModel

    @OptIn(DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)

        mockUsecase = mockk()
        coEvery { mockUsecase.invoke(any()) } returns flowOf(SearchState.Empty)

        viewModel = SearchImagesViewModel(mockUsecase)
    }

    @After
    fun tearDownCoroutineScope() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun initialStateIsEmpty() {
        assertEquals(SearchState.Empty, viewModel.searchViewState.value)
    }

    @Test
    fun searchImagesUpdatesStateToSuccess() = runTest {
        val query = "fruits"
        val images = listOf(mockk<PixabayImageModel>())

        coEvery { mockUsecase.invoke(query) } returns flowOf(SearchState.Success(images))

        viewModel.searchImages(query)

        delay(100)

        val actualState = viewModel.searchViewState.value
        val expectedState = SearchState.Success(images)

        if (actualState is SearchState.Success) {
            assertEquals(expectedState.pixabayImages, actualState.pixabayImages)
        } else {
            Assert.fail("Expected state is not Success")
        }
    }

    @Test
    fun searchImagesUpdatesStateToEmpty() = runBlocking {
        val query = "noresult"

        coEvery { mockUsecase.invoke(query) } returns flowOf(SearchState.Empty)

        viewModel.searchImages(query)

        // Waiting a bit for the state to be updated
        delay(100)

        assertEquals(SearchState.Empty, viewModel.searchViewState.value)
    }

    @Test
    fun onSearchChangeUpdatesSearchQueryState() {
        val query = "new query"
        viewModel.onSearchChange(query)
        assertEquals(query, viewModel.searchQueryState.value)
    }
}
