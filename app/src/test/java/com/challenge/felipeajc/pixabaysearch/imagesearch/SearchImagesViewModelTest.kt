package com.challenge.felipeajc.pixabaysearch.imagesearch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.challenge.felipeajc.pixabaysearch.data.entities.PixabayImageModel
import com.challenge.felipeajc.pixabaysearch.domain.SearchPixabayImageUsecase
import com.challenge.felipeajc.pixabaysearch.domain.SearchState
import com.challenge.felipeajc.pixabaysearch.ui.imagesearch.SearchImagesViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class SearchImagesViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockUsecase: SearchPixabayImageUsecase

    private lateinit var viewModel: SearchImagesViewModel

    @OptIn(DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = SearchImagesViewModel(mockUsecase)
    }

    @Before
    fun setUpCoroutineScope() {
        Dispatchers.setMain(mainThreadSurrogate)
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
    fun searchImagesUpdatesStateToSuccess() = runBlocking {
        val query = "fruits"
        val images = listOf(mock(PixabayImageModel::class.java))

        `when`(mockUsecase(query)).thenReturn(flowOf(SearchState.Success(images)))

        viewModel.searchImages(query)

        assertEquals(SearchState.Success(images), viewModel.searchViewState.value)
    }

    @Test
    fun searchImagesUpdatesStateToEmpty() = runBlocking {
        val query = "noresult"

        `when`(mockUsecase(query)).thenReturn(flowOf(SearchState.Empty))

        viewModel.searchImages(query)

        assertEquals(SearchState.Empty, viewModel.searchViewState.value)
    }

    @Test
    fun onSearchChangeUpdatesSearchQueryState() {
        val query = "new query"
        viewModel.onSearchChange(query)
        assertEquals(query, viewModel.searchQueryState.value)
    }
}
