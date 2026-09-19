package com.fheinke.notero.ui.settings

import com.fheinke.notero.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * A screen that allows the user to change their settings.
 *
 * @param viewModel The [SettingsViewModel] that provides the UI state and handles user interactions.
 * @param modifier The [Modifier] to be applied to the screen.
 */
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(stringResource(R.string.profile), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

        OutlinedTextField(
            value = uiState.userName,
            onValueChange = viewModel::onUserNameChange,
            label = { Text(stringResource(R.string.your_name)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                TextButton(onClick = {
                    viewModel.saveUserName()
                    focusManager.clearFocus()
                }) { Text(stringResource(R.string.save)) }
            }
        )

        HorizontalDivider()

        Text(stringResource(R.string.gender), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

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
                        selected = uiState.gender == value,
                        onClick = { viewModel.onGenderChange(value) },
                        role = Role.RadioButton
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = uiState.gender == value,
                    onClick = null
                )
                Spacer(Modifier.width(8.dp))
                Text(label)
            }
        }

        if (uiState.gender == "female") {
            HorizontalDivider()

            Text(stringResource(R.string.period_tracking), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(stringResource(R.string.enable_period_tracking), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                    Text(
                        stringResource(R.string.period_tracking_description),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = uiState.periodTrackingEnabled,
                    onCheckedChange = viewModel::onPeriodTrackingChange
                )
            }
        }
    }
}