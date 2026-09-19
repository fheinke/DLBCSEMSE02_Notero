package com.fheinke.notero.ui.period

import com.fheinke.notero.R
import com.fheinke.notero.data.local.entities.PeriodRecordEntity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * The main screen for managing period records.
 *
 * @param viewModel The [PeriodViewModel] that provides the UI state and handles user actions.
 * @param onEditRecord A callback function that is invoked when the user wants to edit a record.
 * @param modifier A [Modifier] for this composable.
 *
 * @see PeriodViewModel
 * @see PeriodUiState
 */
@Composable
fun PeriodScreen(
    viewModel: PeriodViewModel,
    onEditRecord: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showStartDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            // Only show the FAB if there is no active period
            val canStart = uiState is PeriodUiState.Empty ||
                    (uiState is PeriodUiState.Success && (uiState as PeriodUiState.Success).activeRecord == null)
            if (canStart) {
                FloatingActionButton(onClick = { showStartDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = stringResource(id = R.string.start_period))
                }
            }
        }
    ) { padding ->
        when (uiState) {
            is PeriodUiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is PeriodUiState.Empty -> {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Text(stringResource(id = R.string.no_entries))
                }
            }
            is PeriodUiState.Success -> {
                val state = uiState as PeriodUiState.Success
                LazyColumn(contentPadding = padding) {
                    state.activeRecord?.let { active ->
                        item {
                            ActivePeriodCard(
                                record = active,
                                onEnd = { viewModel.endPeriod(active) },
                                onEdit = { onEditRecord(active.id) }
                            )
                        }
                    }
                    items(state.records.filter { it.endDate != null }, key = { it.id }) { record ->
                        PeriodRecordCard(
                            record = record,
                            onDelete = { viewModel.deleteRecord(record.id) },
                            onEdit = { onEditRecord(record.id) }
                        )
                    }
                }
            }
        }
    }

    if (showStartDialog) {
        StartPeriodDialog(
            onConfirm = { intensity ->
                viewModel.startPeriod(intensity)
                showStartDialog = false
            },
            onDismiss = { showStartDialog = false }
        )
    }
}

/**
 * A composable that displays the active period record in a card format.
 *
 * @param record The active [PeriodRecordEntity] to display.
 * @param onEnd A callback function that is invoked when the user wants to end the active period.
 * @param onEdit A callback function that is invoked when the user wants to edit the active period.
 *
 * @see PeriodRecordEntity
 */
@Composable
private fun ActivePeriodCard(record: PeriodRecordEntity, onEnd: () -> Unit, onEdit: () -> Unit) {
    val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(
                Icons.Default.Circle, tint = Color.Red,
                contentDescription = stringResource(R.string.active_period),
                modifier = Modifier.size(24.dp)
            )
            Text(stringResource(R.string.active_period), style = MaterialTheme.typography.titleMedium)
            Text(stringResource(R.string.started, record.startDate.format(formatter)))
            record.flowIntensity?.let { Text("${stringResource(R.string.intensity)}: $it / 5") }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onEdit, modifier = Modifier.weight(1f)) {
                    Text(stringResource(R.string.edit))
                }
                Button(onClick = onEnd, modifier = Modifier.weight(1f)) {
                    Text(stringResource(R.string.end_period))
                }
            }
        }
    }
}

/**
 * A composable that displays a past period record in a card format.
 *
 * @param record The [PeriodRecordEntity] to display.
 * @param onDelete A callback function that is invoked when the user wants to delete the record.
 * @param onEdit A callback function that is invoked when the user wants to edit the record.
 *
 * @see PeriodRecordEntity
 */
@Composable
private fun PeriodRecordCard(record: PeriodRecordEntity, onDelete: () -> Unit, onEdit: () -> Unit) {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                "${record.startDate.format(formatter)} – ${record.endDate?.format(formatter) ?: stringResource(R.string.ongoing)}",
                style = MaterialTheme.typography.titleSmall
            )
            record.flowIntensity?.let { Text("${stringResource(R.string.intensity)}: $it / 5") }
            record.note?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
            Row {
                TextButton(onClick = onEdit) { Text(stringResource(R.string.edit)) }
                TextButton(
                    onClick = onDelete,
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) { Text(stringResource(R.string.delete)) }
            }
        }
    }
}

/**
 * A dialog that allows the user to start a new period record with an optional intensity rating.
 *
 * @param onConfirm A callback function that is invoked when the user confirms the start of the period, passing the selected intensity.
 * @param onDismiss A callback function that is invoked when the user dismisses the dialog.
 */
@Composable
private fun StartPeriodDialog(onConfirm: (Int?) -> Unit, onDismiss: () -> Unit) {
    var intensity by remember { mutableStateOf(3f) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.start_period)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("${stringResource(R.string.intensity)}: ${intensity.toInt()} / 5")
                Slider(
                    value = intensity,
                    onValueChange = { intensity = it },
                    valueRange = 1f..5f,
                    steps = 3
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(intensity.toInt()) }) { Text(stringResource(R.string.start_period)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        }
    )
}