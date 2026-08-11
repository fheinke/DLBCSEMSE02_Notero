package com.fheinke.notero.ui.welcome

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fheinke.notero.data.preferences.UserPreferencesRepository
import com.fheinke.notero.ui.overview.MainActivity
import com.fheinke.notero.ui.theme.NoteroTheme

class WelcomeActivity : ComponentActivity() {
    private lateinit var userPreferencesRepository: UserPreferencesRepository

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

    private fun saveUserData(name: String, gender: String?, periodTracking: Boolean) {
        userPreferencesRepository.setUserName(name)
        userPreferencesRepository.setUserGender(gender)
        userPreferencesRepository.setPeriodTrackingEnabled(periodTracking)
        userPreferencesRepository.setOnboardingCompleted(true)
    }

    private fun navigateToMainApp() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}

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
                title = { Text("Welcome to Notero") }
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
                text = "Let's get started!",
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
                label = { Text("Your Name") },
                placeholder = { Text("Enter your name") },
                isError = showError,
                supportingText = if (showError) {
                    { Text("Please enter your name") }
                } else null,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Gender Selection
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Your Gender (Optional)",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )

                listOf(
                    "male" to "Male",
                    "female" to "Female",
                    "other" to "Other",
                    null to "Not Specified"
                ).forEach { (value, label) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedGender == value,
                            onClick = { selectedGender = value }
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
                        text = "Enable Period Tracking",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Track your menstrual cycle to receive insights and reminders.",
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
                        Text(if (periodTrackingEnabled) "Active" else "Inactive")
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
                Text("Let's Go")
            }
        }
    }
}