package com.fheinke.notero.ui.welcome

import com.fheinke.notero.R
import com.fheinke.notero.data.repository.UserPreferencesRepository
import com.fheinke.notero.ui.overview.MainActivity
import com.fheinke.notero.ui.theme.NoteroTheme
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Activity that welcomes the user and collects their name, gender, and period tracking preference.
 *
 * This activity is shown when the app is launched for the first time, and it saves the user's preferences using [UserPreferencesRepository].
 */
class WelcomeActivity : ComponentActivity() {
    private lateinit var userPreferencesRepository: UserPreferencesRepository

    /**
     * Called when the activity is starting. This is where most initialization should go.
     * Here, we set up the UI using Jetpack Compose and handle user input.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down, this Bundle contains the data it most recently supplied. Otherwise, it is null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        userPreferencesRepository = UserPreferencesRepository(applicationContext)

        setContent {
            NoteroTheme {
                WelcomeScreen(
                    onComplete = { name, gender, periodTrackingEnabled ->
                        saveUserData(name, gender, periodTrackingEnabled)
                        navigateToMainApp()
                    }
                )
            }
        }
    }

    /**
     * Saves the user's data to the [UserPreferencesRepository].
     *
     * @param name The user's name.
     * @param gender The user's gender (can be null).
     * @param periodTracking Whether period tracking is enabled.
     */
    private fun saveUserData(name: String, gender: String?, periodTracking: Boolean) {
        userPreferencesRepository.setUserName(name)
        userPreferencesRepository.setUserGender(gender)
        userPreferencesRepository.setPeriodTrackingEnabled(periodTracking)
        userPreferencesRepository.setOnboardingCompleted(true)
    }

    /**
     * Navigates to the main application activity ([MainActivity]) and finishes the current activity.
     */
    private fun navigateToMainApp() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}

/**
 * Composable function that displays the welcome screen where the user can input their name, select their gender, and choose whether to enable period tracking.
 *
 * @param onComplete Callback function that is invoked when the user completes the form. It provides the user's name, selected gender, and period tracking preference.
 * @param modifier Modifier to be applied to the layout.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(
    onComplete: (name: String, gender: String?, periodTracking: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf<String?>(null) }
    var periodTrackingEnabled by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.welcome_text)) }
            )
        }
    ) {
        paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = stringResource(R.string.welcome_subtitle),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            // Name Input
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    showError = false
                },
                label = { Text(stringResource(R.string.your_name)) },
                placeholder = { Text(stringResource(R.string.enter_your_name)) },
                isError = showError,
                supportingText = if (showError) {
                    { Text(stringResource(R.string.enter_your_name)) }
                } else null,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Gender Selection
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(R.string.your_gender_optional),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )

                listOf(
                    "male" to stringResource(R.string.male),
                    "female" to stringResource(R.string.female),
                    "other" to stringResource(R.string.other),
                    null to stringResource(R.string.not_specified)
                ).forEach { (value, label) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = selectedGender == value,
                                onClick = { selectedGender = value },
                                role = Role.RadioButton
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedGender == value,
                            onClick = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(label)
                    }
                }
            }

            // Period Tracking (only for female)
            if (selectedGender == "female") {
                HorizontalDivider()

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(R.string.enable_period_tracking),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = stringResource(R.string.period_tracking_description),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            checked = periodTrackingEnabled,
                            onCheckedChange = { periodTrackingEnabled = it }
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(if (periodTrackingEnabled) stringResource(R.string.active) else stringResource(R.string.inactive))
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (name.isBlank()) {
                        showError = true
                    } else {
                        onComplete(name.trim(), selectedGender, periodTrackingEnabled)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.welcome_subtitle))
            }
        }
    }
}