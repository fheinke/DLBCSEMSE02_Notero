package com.fheinke.notero.ui.overview

import com.fheinke.notero.data.local.entities.JournalEntryEntity

/**
 * Represents the UI state for the Journal Overview screen.
 *
 * This sealed class defines three possible states:
 * 1. Loading: Indicates that the UI is currently loading data.
 * 2. Empty: Indicates that there are no journal entries to display.
 * 3. Success: Indicates that journal entries are available and provides the list of entries.
 */
sealed class JournalOverviewUiState {
    // Represents the loading state of the UI.
    object Loading : JournalOverviewUiState()
    // Represents the empty state of the UI when there are no journal entries.
    object Empty : JournalOverviewUiState()
    // Represents the success state of the UI when journal entries are available.
    data class Success(
        val journalEntries: List<JournalEntryEntity>
    ) : JournalOverviewUiState()
}
