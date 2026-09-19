package com.fheinke.notero.ui.overview

import com.fheinke.notero.R
import com.fheinke.notero.ui.theme.NoteroTheme
import com.fheinke.notero.NoteroApplication
import com.fheinke.notero.data.repository.UserPreferencesRepository
import com.fheinke.notero.ui.detail.EntryDetailScreen
import com.fheinke.notero.ui.detail.EntryDetailViewModel
import com.fheinke.notero.ui.editor.EntryEditorScreen
import com.fheinke.notero.ui.editor.EntryEditorViewModel
import com.fheinke.notero.ui.period.PeriodEditorScreen
import com.fheinke.notero.ui.period.PeriodEditorViewModel
import com.fheinke.notero.ui.period.PeriodScreen
import com.fheinke.notero.ui.period.PeriodViewModel
import com.fheinke.notero.ui.settings.SettingsScreen
import com.fheinke.notero.ui.settings.SettingsViewModel
import com.fheinke.notero.ui.welcome.WelcomeActivity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

/**
 * MainActivity is the entry point of the Notero application. It initializes the necessary ViewModels and sets up the navigation structure.
 *
 * The activity checks if the onboarding process has been completed. If not, it redirects the user to the WelcomeActivity. It also manages the visibility of the PERIODRECORD tab based on user settings.
 *
 * @see SettingsViewModel
 * @see JournalOverviewViewModel
 * @see PeriodViewModel
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as NoteroApplication

        val userPreferencesRepository = UserPreferencesRepository(this)

        if (!userPreferencesRepository.isOnboardingCompleted()) {
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
            return
        }

        val settingsViewModel: SettingsViewModel by viewModels {
            SettingsViewModel.factory(userPreferencesRepository)
        }

        val journalViewModel: JournalOverviewViewModel by viewModels {
            JournalOverviewViewModel.factory(app.journalRepository)
        }

        val periodViewModel: PeriodViewModel by viewModels {
            PeriodViewModel.factory(app.periodRepository)
        }

        enableEdgeToEdge()

        setContent {
            NoteroTheme {
                NoteroApp(settingsViewModel = settingsViewModel, journalViewModel = journalViewModel, periodViewModel = periodViewModel)
            }
        }
    }
}

/**
 * NoteroApp is the main composable function that sets up the application's UI structure, including the top app bar, bottom navigation bar, and navigation host.
 *
 * @param settingsViewModel The ViewModel responsible for managing user settings.
 * @param journalViewModel The ViewModel responsible for managing journal entries.
 * @param periodViewModel The ViewModel responsible for managing period tracking records.
 * @param modifier A [Modifier] for styling and layout adjustments.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteroApp(
    settingsViewModel: SettingsViewModel,
    journalViewModel: JournalOverviewViewModel,
    periodViewModel: PeriodViewModel,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val startDestination = Destination.JOURNAL

    val settingsUiState by settingsViewModel.uiState.collectAsStateWithLifecycle()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val currentTitle = Destination.entries
        .find { it.route == currentRoute }
        ?.let { stringResource(id = it.labelRes)}
        ?: stringResource(startDestination.labelRes)

    // Only show the PERIODRECORD tab if period tracking is enabled in settings
    val visibleDestinations = remember(settingsUiState.periodTrackingEnabled) {
        Destination.entries.filter {
            it != Destination.PERIODRECORD || settingsUiState.periodTrackingEnabled
        }
    }

    val selectedIndex = visibleDestinations.indexOfFirst { it.route == currentRoute }

    // If period tracking is disabled and the user is currently on the PERIODRECORD screen, navigate back to JOURNAL
    LaunchedEffect(settingsUiState.periodTrackingEnabled) {
        if (!settingsUiState.periodTrackingEnabled &&
            navController.currentDestination?.route == Destination.PERIODRECORD.route
        ) {
            navController.navigate(Destination.JOURNAL.route) {
                popUpTo(Destination.JOURNAL.route) { inclusive = false }
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text(currentTitle) })
        },
        bottomBar = {
            NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                visibleDestinations.forEachIndexed { index, destination ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = {
                            navController.navigate(route = destination.route)
                        },
                        icon = {
                            Icon(
                                destination.icon,
                                contentDescription = stringResource(destination.labelRes)
                            )
                        },
                        label = { Text(stringResource(destination.labelRes)) }
                    )
                }
            }
        }
    ) { contentPadding ->
        AppNavHost(
            navController,
            startDestination,
            settingsViewModel = settingsViewModel,
            journalViewModel = journalViewModel,
            periodViewModel = periodViewModel,
            modifier = Modifier.padding(contentPadding)
        )
    }
}

/**
 * Destination enum represents the different screens in the Notero application, each with a unique route, label resource ID, and icon.
 *
 * @property route The navigation route for the destination.
 * @property labelRes The string resource ID for the destination's label.
 * @property icon The icon representing the destination.
 */
enum class Destination(
    val route: String,
    @StringRes val labelRes: Int,
    val icon: ImageVector,
) {
    JOURNAL("journal", R.string.nav_journal, Icons.Default.Book),
    PERIODRECORD("periodRecord", R.string.nav_period_tracker, Icons.Default.CalendarMonth),
    SETTINGS("settings", R.string.nav_settings, Icons.Default.Settings)
}

/**
 * AppNavHost sets up the navigation graph for the Notero application, defining the composable destinations and their corresponding screens.
 *
 * @param navController The [NavHostController] used for navigation between screens.
 * @param startDestination The initial destination to display when the app starts.
 * @param settingsViewModel The ViewModel responsible for managing user settings.
 * @param journalViewModel The ViewModel responsible for managing journal entries.
 * @param periodViewModel The ViewModel responsible for managing period tracking records.
 * @param modifier A [Modifier] for styling and layout adjustments.
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: Destination,
    settingsViewModel: SettingsViewModel,
    journalViewModel: JournalOverviewViewModel,
    periodViewModel: PeriodViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.route,
        modifier = modifier
    ) {
        composable(Destination.JOURNAL.route) {
            JournalOverviewScreen(
                viewModel = journalViewModel,
                onEntryClick = { id -> navController.navigate("detail/$id") },
                onAddClick = { navController.navigate("editor") }
            )
        }
        composable(
            route = "detail/{entryId}",
            arguments = listOf(navArgument("entryId") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("entryId") ?: return@composable
            val app = (LocalContext.current.applicationContext as NoteroApplication)
            val viewModel: EntryDetailViewModel = viewModel(
                factory = EntryDetailViewModel.factory(app.journalRepository, id)
            )
            EntryDetailScreen(
                entryId = id,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onEdit = { navController.navigate("editor?entryId=$it") }
            )
        }
        composable(
            route = "editor?entryId={entryId}",
            arguments = listOf(navArgument("entryId") { type = NavType.LongType; defaultValue = -1L })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("entryId").takeIf { it != -1L }
            val app = (LocalContext.current.applicationContext as NoteroApplication)
            val viewModel: EntryEditorViewModel = viewModel(
                factory = EntryEditorViewModel.factory(app.journalRepository, id)
            )
            EntryEditorScreen(
                entryId = id,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Destination.PERIODRECORD.route) {
            PeriodScreen(
                viewModel = periodViewModel,
                onEditRecord = { id -> navController.navigate("period-editor?recordId=$id") }
            )
        }
        composable(
            route = "period-editor?recordId={recordId}",
            arguments = listOf(navArgument("recordId") { type = NavType.LongType; defaultValue = -1L })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("recordId").takeIf { it != -1L }
            val app = LocalContext.current.applicationContext as NoteroApplication
            val vm: PeriodEditorViewModel = viewModel(
                factory = PeriodEditorViewModel.factory(app.periodRepository, id)
            )
            PeriodEditorScreen(recordId = id, viewModel = vm, onBack = { navController.popBackStack() })
        }
        composable(Destination.SETTINGS.route) {
            SettingsScreen(viewModel = settingsViewModel)
        }
    }
}
