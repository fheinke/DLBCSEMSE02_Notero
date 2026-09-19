package com.fheinke.notero.ui.detail

import com.fheinke.notero.R
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * Composable function to display the details of a specific entry.
 *
 * @param entryId The ID of the entry to display.
 * @param viewModel The ViewModel that provides the entry data.
 * @param onBack Callback function to handle back navigation.
 * @param onEdit Callback function to handle editing the entry.
 * @param modifier Modifier for styling and layout adjustments.
 *
 * @see EntryDetailViewModel
 * @see EntryDetailUiState
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryDetailScreen(
    entryId: Long,
    viewModel: EntryDetailViewModel,
    onBack: () -> Unit,
    onEdit: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.entry_detail)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (uiState is EntryDetailUiState.Success) {
                        IconButton(onClick = { onEdit(entryId) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            )
        }
    ) { padding ->
        when (uiState) {
            is EntryDetailUiState.Loading -> {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is EntryDetailUiState.NotFound -> {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Text(stringResource(R.string.entry_not_found), style = MaterialTheme.typography.bodyLarge)
                }
            }
            is EntryDetailUiState.Success -> {
                val entry = (uiState as EntryDetailUiState.Success).entry
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(entry.title, style = MaterialTheme.typography.headlineMedium)
                    Text(
                        entry.entryDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    HorizontalDivider()
                    Text(entry.text, style = MaterialTheme.typography.bodyLarge)
                    entry.mood?.let {
                        Text("${stringResource(R.string.mood)}: $it / 5", style = MaterialTheme.typography.bodyMedium)
                    }
                    entry.sleepScore?.let {
                        Text("${stringResource(R.string.sleep)}: $it / 5", style = MaterialTheme.typography.bodyMedium)
                    }
                    entry.stressLevel?.let {
                        Text("${stringResource(R.string.stress)}: $it / 5", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.delete_entry)) },
            text = { Text(stringResource(R.string.delete_entry_confirmation)) },
            confirmButton = {
                TextButton(onClick = { viewModel.deleteEntry(onBack) }) {
                    Text(stringResource(R.string.delete), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text(stringResource(R.string.cancel)) }
            }
        )
    }
}