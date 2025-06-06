package com.example.starzplay_videoappcodechallenge// Imports for unit test

import com.example.library.model.MediaItem
import com.example.library.repository.MediaRepository
import com.example.starzplay_videoappcodechallenge.ui.main.MainEffect
import com.example.starzplay_videoappcodechallenge.ui.main.MainViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest


import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.After


@OptIn(ExperimentalCoroutinesApi::class)
@ExperimentalCoroutinesApi
class MainViewModelTest {

    private lateinit var repository: MediaRepository
    private lateinit var viewModel: MainViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        viewModel = MainViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `perform Search returns success state with results`() = runTest {
        // Given
        val query = "Avatar"
        val fakeMediaItems = listOf(
            MediaItem(id = 1, mediaType = "movie", overview = "", originalLanguage = "en", posterPath = "/test.jpg", backdropPath = "/test.jpg"),
            MediaItem(id = 1, mediaType = "movie", overview = "", originalLanguage = "en", posterPath = "/test.jpg", backdropPath = "/test.jpg"),
            MediaItem(id = 1, mediaType = "movie", overview = "", originalLanguage = "en", posterPath = "/test.jpg", backdropPath = "/test.jpg"),
        )
        coEvery { repository.getSearchResults(query) } returns fakeMediaItems

        // When
        viewModel.performSearch(query)
        advanceUntilIdle()

        // Then
        val expectedGrouped = fakeMediaItems
            .filter { it.mediaType != null }
            .groupBy { it.mediaType!! }
            .toSortedMap()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals(expectedGrouped, state.mediaByType)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `performSearch emits error effect and resets loading on exception`() = runTest {
        // Given
        val query = "Avatar"
        val errorMessage = "Network error"
        coEvery { repository.getSearchResults(query) } throws RuntimeException(errorMessage)

        // When
        viewModel.performSearch(query)
        advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        val effect = viewModel.effect.value

        // Loading should be false and mediaByType empty
        assertFalse(state.isLoading)
        assertTrue(state.mediaByType.isEmpty())

        // Effect should contain the error message
        assertTrue(effect is MainEffect.ShowError)
        assertEquals("Error: $errorMessage", (effect as MainEffect.ShowError).message)
    }

}
