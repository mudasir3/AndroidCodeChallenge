package com.example.starzplay_videoappcodechallenge.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.library.model.MediaItem
import com.example.starzplay_videoappcodechallenge.R
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun DetailScreen(navController: NavController, mediaItem: MediaItem) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // An AsyncImagePainter for loading remote image
        val painter = rememberAsyncImagePainter(model = mediaItem.fullDetailPosterUrl)
        val painterState = painter.state

        // If image loading fails, show a local placeholder image
        if (painterState is AsyncImagePainter.State.Error) {
            Image(
                painter = painterResource(id = R.drawable.image_placeholder), // Fallback image
                contentDescription = mediaItem.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
        } else {
            // Show remote image using AsyncImagePainter
            Image(
                painter = painter,
                contentDescription = mediaItem.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
        }

        // Display media title
        Text(
            text = mediaItem.title ?: "No title",
            style = MaterialTheme.typography.headlineSmall
        )

        // Display media overview/description
        Text(
            text = mediaItem.overview ?: "No description",
            style = MaterialTheme.typography.bodyLarge
        )

        // Show Play button only if media type is movie or tv
        if (mediaItem.mediaType == "movie" || mediaItem.mediaType == "tv") {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    // Encode image URL to safely pass in navigation
                    val encodedUrl = URLEncoder.encode(
                        mediaItem.fullBackdropUrl,
                        StandardCharsets.UTF_8.toString()
                    )
                    // Navigate to PlayerScreen with imageUrl
                    navController.navigate("player/$encodedUrl")
                }
            ) {
                Text("Play")
            }
        }
    }
}

