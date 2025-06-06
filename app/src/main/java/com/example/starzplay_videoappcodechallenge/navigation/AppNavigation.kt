package com.example.starzplay_videoappcodechallenge.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.library.model.MediaItem
import com.example.starzplay_videoappcodechallenge.ui.detail.DetailScreen
import com.example.starzplay_videoappcodechallenge.ui.main.MainScreen
import com.example.starzplay_videoappcodechallenge.ui.player.PlayerScreen
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


// Main navigation composable setting up all app routes
@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    // Define the NavHost with a starting destination
    NavHost(navController = navController, startDestination = "main") {

        // Main screen route
        composable("main") {
            MainScreenWithNav(navController)
        }

        // Detail screen route with serialized mediaItem passed as an argument
        composable("detail/{mediaItem}") { backStackEntry ->
            // Retrieve and decode the mediaItem argument from the back stack
            val mediaItemJson = backStackEntry.arguments?.getString("mediaItem")
            val json = Uri.decode(mediaItemJson)

            // Deserialize the JSON string to a MediaItem object
            val mediaItem = Json.decodeFromString<MediaItem>(json)

            // Show the DetailScreen with the deserialized mediaItem
            DetailScreen(navController, mediaItem)
        }

        // Player screen route with imageUrl passed as an argument
        composable("player/{imageUrl}") { backStackEntry ->
            // Retrieve and decode the imageUrl argument
            val imageUrlEncoded = backStackEntry.arguments?.getString("imageUrl")
            val imageUrl = URLDecoder.decode(imageUrlEncoded, StandardCharsets.UTF_8.toString())

            // Show the PlayerScreen if imageUrl is valid
            imageUrl?.let {
                PlayerScreen(it)
            }
        }
    }
}

// Wrapper composable for MainScreen that passes a navigation lambda
@Composable
fun MainScreenWithNav(navController: NavHostController) {
    MainScreen(onItemClick = { mediaItem ->
        // Serialize the clicked mediaItem to JSON
        val json = Json.encodeToString(mediaItem)

        // URL-encode the JSON string to safely pass in the route
        val encodedJson = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())

        // Navigate to the detail screen with encoded mediaItem
        navController.navigate("detail/$encodedJson")
    })
}
