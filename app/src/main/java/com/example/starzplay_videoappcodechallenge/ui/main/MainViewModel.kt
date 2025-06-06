package com.example.starzplay_videoappcodechallenge.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.library.repository.MediaRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class MainViewModel(private val repository: MediaRepository) : ViewModel() {

    // Backing property for UI state (isLoading + grouped media items)
    private val _state = MutableStateFlow(MainViewState())

    // Public immutable state for the UI to observe
    open val state: StateFlow<MainViewState> = _state.asStateFlow()

    // Effect stream to emit one-time events like errors/snackbars
    private val _effect = MutableStateFlow<MainEffect?>(null)

    // Public effect for UI to collect and respond to one-time events
    val effect: StateFlow<MainEffect?> = _effect.asStateFlow()

    // Intent processor from UI (MVI pattern)
    fun processIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.Search -> performSearch(intent.query)
        }
    }

    // Debounce job for managing rapid search inputs
    private var searchJob: Job? = null

    /**
     * Performs search based on the given query.
     * Handles loading state, success result grouping, and error handling.
     */
    open fun performSearch(query: String) {
        // Cancel any previous ongoing search (debounce)
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            // Show loading state
            _state.value = _state.value.copy(isLoading = true)

            try {
                // Fetch search results from repository
                val results = repository.getSearchResults(query)

                // Group results by non-null mediaType and sort keys alphabetically
                val grouped = results
                    .filter { it.mediaType != null }
                    .groupBy { it.mediaType!! }
                    .toSortedMap()

                // Update UI state with grouped results and stop loading
                _state.value = MainViewState(
                    isLoading = false,
                    mediaByType = grouped
                )

            } catch (e: Exception) {
                // On error, emit effect for UI to show error (Snackbar/Toast)
                _effect.value = MainEffect.ShowError("Error: ${e.localizedMessage}")

                // Stop loading and clear previous results
                _state.value = _state.value.copy(
                    isLoading = false,
                    mediaByType = emptyMap()
                )
            }
        }
    }
}
