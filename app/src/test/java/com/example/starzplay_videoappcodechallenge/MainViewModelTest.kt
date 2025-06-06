package com.example.starzplay_videoappcodechallenge// Imports for unit test

import com.example.library.api.TmdbApi
import com.example.library.model.MediaItem
import com.example.library.model.SearchResponse
import com.example.library.repository.MediaRepository
import com.example.starzplay_videoappcodechallenge.ui.main.MainEffect
import com.example.starzplay_videoappcodechallenge.ui.main.MainIntent
import com.example.starzplay_videoappcodechallenge.ui.main.MainViewModel
import com.example.starzplay_videoappcodechallenge.ui.main.MainViewState
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import io.mockk.coEvery
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.rules.TestRule
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock


import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.mock
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.Headers.Companion.headersOf
import org.junit.After


@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private lateinit var repository: MediaRepository
    private lateinit var viewModel: MainViewModel
    private lateinit var api: TmdbApi
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        api = mock()
        repository = MediaRepository(api) // assuming it's a constructor param

        viewModel = MainViewModel(repository)
    }

    @Test
    fun `performSearch handles API error and sets effect`() = runTest {
        val query = "ErrorQuery"
        `when`(repository.getSearchResults(query)).thenThrow(RuntimeException("API failed"))

        viewModel.processIntent(MainIntent.Search(query))
        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertTrue(state.mediaByType.isEmpty())

        val effect = viewModel.effect.value
        assertTrue(effect is MainEffect.ShowError)
        assertEquals("Error: API failed", (effect as MainEffect.ShowError).message)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }



//    //@get:Rule
//    //val rule: TestRule = InstantTaskExecutorRule()
//
//    private lateinit var api: TmdbApi
//    private lateinit var repository: MediaRepository
//    private lateinit var viewModel: MainViewModel
//
//    @Before
//    fun setUp() {
//        api = mock(TmdbApi::class.java)
//        repository = MediaRepository(api)
//        viewModel = MainViewModel(repository)
//    }
//
//    @Test
//    fun `performSearch returns success state with results`() = runTest {
//        val mockResponse = SearchResponse(
//            results = listOf(
//                MediaItem(id = 1, mediaType = "movie", overview = "", originalLanguage = "en", posterPath = "/test.jpg", backdropPath = "/test.jpg"),
//                MediaItem(id = 1, mediaType = "movie", overview = "", originalLanguage = "en", posterPath = "/test.jpg", backdropPath = "/test.jpg"),
//            ),
//            page = 1,
//            total_results = 1,
//            total_pages = 1
//        )
//
//        `when`(api.search("batman")).thenReturn(mockResponse)
//
//        viewModel.performSearch("batman")
//
//        val state = viewModel.state.value
//        assertTrue(state is MainViewState)
////        assertEquals(2, (state as MainViewState.).items.size)
//    }
//
//    @Test
//    fun `performSearch handles API error`() = runTest {
//        `when`(api.search("invalid")).thenThrow(RuntimeException("Network error"))
//
//        viewModel.performSearch("invalid")
//
//        val state = viewModel.state.value
//        assertTrue(state is MainViewState)
//        //assertEquals("Network error", (state as MainUiState.Error).message)
//    }
}
