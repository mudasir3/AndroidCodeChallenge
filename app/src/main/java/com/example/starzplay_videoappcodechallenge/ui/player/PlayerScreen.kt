package com.example.starzplay_videoappcodechallenge.ui.player

import android.app.Activity
import android.content.pm.ActivityInfo
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.rememberAsyncImagePainter

@Composable
fun PlayerScreen(imageUrl: String) {
    val context = LocalContext.current
    val activity = context as? Activity
    val decorView = LocalView.current

    // Force screen orientation to landscape mode
    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

    //  Hide system UI for an immersive fullscreen experience
    decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            )

    // State to toggle between image preview and video player
    var playVideo by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        if (!playVideo) {
            //  Show preview image
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )

            //  Play button to switch to video player
            Button(
                onClick = { playVideo = true },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Text("Play Video")
            }
        } else {
            //  Show video player once play button is clicked
            VideoPlayer(
                context = context,
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
            )
        }
    }
}

@Composable
fun VideoPlayer(context: android.content.Context, videoUrl: String) {
    // Initialize and remember ExoPlayer instance
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(Uri.parse(videoUrl))) // Set video source
            prepare()      // Prepare the player
            playWhenReady = true // Auto-play video when ready
        }
    }

    // Release ExoPlayer when composable is disposed
    DisposableEffect(Unit) {
        onDispose { exoPlayer.release() }
    }

    // Display the video using AndroidView with ExoPlayer's PlayerView
    AndroidView(
        factory = {
            PlayerView(context).apply {
                player = exoPlayer         // Set the player
                useController = true       // Show playback controls
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        },
        modifier = Modifier.fillMaxSize() // Fill entire screen
    )
}
