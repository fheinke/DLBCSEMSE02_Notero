package com.fheinke.notero

import com.fheinke.notero.ui.theme.NoteroTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlaylistAddCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            NoteroTheme {
                NoteroApp()
            }
        }
    }
}

enum class Destination(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val contentDescription: String
) {
    NOTERO("notero", "Notero", Icons.Default.MusicNote, "Notero"),
    ALBUM("album", "Album", Icons.Default.Album, "Album"),
    PLAYLISTS("playlist", "Playlist", Icons.Default.PlaylistAddCircle, "Playlist")
}

@Composable
fun NoteroScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Notero Screen")
    }
}

@Composable
fun AlbumScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Album Screen")
    }
}

@Composable
fun PlaylistScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Playlist Screen")
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: com.fheinke.notero.Destination,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.route,
        modifier = modifier
    ) {
        composable(com.fheinke.notero.Destination.NOTERO.route) { NoteroScreen() }
        composable(com.fheinke.notero.Destination.ALBUM.route) { AlbumScreen() }
        composable(com.fheinke.notero.Destination.PLAYLISTS.route) { PlaylistScreen() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteroApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val startDestination = com.fheinke.notero.Destination.NOTERO
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val currentTitle = com.fheinke.notero.Destination.entries.find { it.route == currentRoute }?.label ?: startDestination.label

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text(currentTitle) })
        },
        bottomBar = {
            NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                com.fheinke.notero.Destination.entries.forEachIndexed { index, destination ->
                    NavigationBarItem(
                        selected = selectedDestination == index,
                        onClick = {
                            navController.navigate(route = destination.route)
                            selectedDestination = index
                        },
                        icon = {
                            Icon(
                                destination.icon,
                                contentDescription = destination.contentDescription
                            )
                        },
                        label = { Text(destination.label) }
                    )
                }
            }
        }
    ) { contentPadding ->
        com.fheinke.notero.AppNavHost(
            navController,
            startDestination,
            modifier = Modifier.padding(contentPadding)
        )
    }
}