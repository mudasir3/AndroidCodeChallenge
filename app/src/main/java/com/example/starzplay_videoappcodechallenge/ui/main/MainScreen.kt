package com.example.starzplay_videoappcodechallenge.ui.main


import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.library.model.MediaItem
import com.example.starzplay_videoappcodechallenge.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel = koinViewModel(),
    onItemClick: (MediaItem) -> Unit
) {
    // Observe state from ViewModel
    val state by viewModel.state.collectAsState()

    // Mutable state for user search input
    var searchQuery by remember { mutableStateOf("") }

    // Perform default search when the screen is first composed
    LaunchedEffect(Unit) {
        viewModel.processIntent(MainIntent.Search("A"))
    }

    Column(modifier = Modifier.fillMaxSize()) {

        // 🔍 Search input field
        SearchBox(searchQuery) { query ->
            searchQuery = query
            viewModel.processIntent(MainIntent.Search(query))
        }

        // 📦 Main content: LazyColumn for media groups by type
        if (!state.isLoading && state.mediaByType.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp)
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    // Loop over each media type category
                    state.mediaByType.forEach { (mediaType, items) ->
                        item {
                            // Title for each media type (e.g., MOVIE, TV)
                            Text(
                                text = mediaType.uppercase(),
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // 📱 Responsive layout: calculate image size based on screen width
                            val configuration = LocalConfiguration.current
                            val screenWidth = configuration.screenWidthDp.dp
                            val horizontalPadding = 16.dp
                            val spacingBetweenItems = 8.dp * 3 // spacing between 4 items

                            val totalSpacing = horizontalPadding + spacingBetweenItems
                            val imageWidth = (screenWidth - totalSpacing) / 4
                            val imageHeight = imageWidth * 1.2f

                            Column(modifier = Modifier.fillMaxWidth()) {
                                // Horizontal scrollable row of posters
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
                                        .padding(vertical = 2.dp)
                                ) {
                                    items(items.size) { index ->
                                        val item = items[index]

                                        Box(
                                            modifier = Modifier.padding(vertical = 5.dp)
                                        ) {
                                            val painter =
                                                rememberAsyncImagePainter(item.fullPosterUrl)
                                            val painterState = painter.state

                                            when (painterState) {
                                                is AsyncImagePainter.State.Error -> {
                                                    // Show fallback placeholder on load failure
                                                    Image(
                                                        painter = painterResource(id = R.drawable.image_placeholder),
                                                        contentDescription = "Placeholder",
                                                        contentScale = ContentScale.Crop,
                                                        modifier = Modifier
                                                            .width(imageWidth)
                                                            .height(imageHeight)
                                                            .padding(vertical = 2.dp)
                                                            .clickable { onItemClick(item) }
                                                    )
                                                }

                                                else -> {
                                                    // Load actual image
                                                    Image(
                                                        painter = painter,
                                                        contentDescription = item.displayTitle,
                                                        contentScale = ContentScale.Crop,
                                                        modifier = Modifier
                                                            .width(imageWidth)
                                                            .height(imageHeight)
                                                            .padding(vertical = 2.dp)
                                                            .clickable { onItemClick(item) }
                                                    )

                                                    // Show loading indicator while image is loading
                                                    if (painterState is AsyncImagePainter.State.Loading) {
                                                        Box(
                                                            modifier = Modifier
                                                                .width(imageWidth)
                                                                .height(imageHeight)
                                                                .padding(vertical = 2.dp),
                                                            contentAlignment = Alignment.Center
                                                        ) {
                                                            CircularProgressIndicator(
                                                                modifier = Modifier.size(24.dp),
                                                                strokeWidth = 2.dp
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                    }
                }
            }
        }

        // 🌀 Loading or Empty State
        if (state.isLoading || state.mediaByType.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 120.dp),
                contentAlignment = Alignment.Center
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text("No results found", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
fun SearchBox(
    query: String,
    onQueryChange: (String) -> Unit
) {
    // Text field with trailing clear button
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        label = { Text("Search") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        trailingIcon = {
            // Show clear (X) icon if there is any input
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear Search"
                    )
                }
            }
        }
    )
}
