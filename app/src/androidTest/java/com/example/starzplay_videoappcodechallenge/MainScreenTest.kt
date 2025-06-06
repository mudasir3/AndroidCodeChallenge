package com.example.starzplay_videoappcodechallenge// Imports for Compose UI test
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.library.model.MediaItem
import com.example.starzplay_videoappcodechallenge.ui.main.MainScreen
import com.example.starzplay_videoappcodechallenge.ui.main.MainViewModel
import com.example.starzplay_videoappcodechallenge.ui.main.MainViewState
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

class MainScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun mainScreen_displaysMediaItems() {
        val mediaItems = listOf(
            MediaItem(id = 1, mediaType = "movie", overview = "", originalLanguage = "en", posterPath = "/test.jpg", backdropPath = "/test.jpg"),
            MediaItem(id = 2, mediaType = "tv", overview = "", originalLanguage = "en", posterPath = "/test2.jpg", backdropPath = "/test.jpg")
        ).groupBy { it.mediaType }

        composeTestRule.setContent {
            MainScreen(
                viewModel = FakeMainViewModel(mediaByType = mediaItems),
                onItemClick = {}
            )
        }

        // Check if media types are displayed
        composeTestRule.onNodeWithText("MOVIE").assertIsDisplayed()
        composeTestRule.onNodeWithText("TV").assertIsDisplayed()

        // Check if images exist by contentDescription (displayTitle)
        mediaItems.values.flatten().forEach { item ->
            composeTestRule.onNodeWithContentDescription(item.displayTitle).assertIsDisplayed()
        }
    }
}

// Fake ViewModel for testing Compose UI
class FakeMainViewModel(private val mediaByType: Map<String, List<MediaItem>>) : MainViewModel(mock()) {
    override val state = MutableStateFlow(MainViewState(isLoading = false, mediaByType = mediaByType))

    override fun performSearch(query: String) {}
}
