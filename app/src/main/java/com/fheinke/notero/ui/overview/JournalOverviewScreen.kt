package com.fheinke.notero.ui.overview

import com.fheinke.notero.R
import com.fheinke.notero.data.local.entities.JournalEntryEntity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * Composable function to display the journal overview screen.
 *
 * @param viewModel The ViewModel providing the UI state.
 * @param onEntryClick Callback when a journal entry is clicked, passing the entry ID.
 * @param onAddClick Callback when the add button is clicked.
 * @param modifier Modifier for styling and layout adjustments.
 *
 * @see JournalOverviewViewModel
 * @see JournalOverviewUiState
 * @see JournalEntryEntity
 */
@Composable
fun JournalOverviewScreen(
    viewModel: JournalOverviewViewModel,
    onEntryClick: (Long) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Entry")
            }
        }
    ) {
        padding ->
        when(uiState) {
            is JournalOverviewUiState.Loading -> {
                // Show loading indicator
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is JournalOverviewUiState.Empty -> {
                // Show empty state message or UI
                Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Text(text = stringResource(R.string.no_entries))
                }
            }
            is JournalOverviewUiState.Success -> {
                // Show list of journal entries
                val entries = (uiState as JournalOverviewUiState.Success).journalEntries
                LazyColumn(contentPadding = padding) {
                    items(entries, key = { it.id }) { entry ->
                        JournalEntryCard(entry = entry, onClick = { onEntryClick(entry.id) })
                    }
                }
            }
        }
    }
}

/**
 * Composable function to display a single journal entry card.
 *
 * @param entry The journal entry data to display.
 * @param onClick Callback when the card is clicked.
 *
 * @see JournalEntryEntity
 */
@Composable
private fun JournalEntryCard(
    entry: JournalEntryEntity,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = entry.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = entry.entryDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            entry.mood?.let { mood ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${stringResource(R.string.mood)}: $mood / 5",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}