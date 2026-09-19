package com.fheinke.notero.ui.overview

import com.fheinke.notero.data.repository.JournalRepository
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.StateFlow

/**
 * ViewModel for the Journal Overview screen.
 *
 * @param journalRepository The repository to fetch journal entries from.
 *
 * @see JournalOverviewUiState
 */
class JournalOverviewViewModel(
    journalRepository: JournalRepository
) : ViewModel() {
    val uiState: StateFlow<JournalOverviewUiState> = journalRepository
        .getAll()
        .map { entries ->
            if (entries.isEmpty()) {
                JournalOverviewUiState.Empty
            } else {
                JournalOverviewUiState.Success(entries)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = JournalOverviewUiState.Loading
        )

    companion object {
        /**
         * Creates a factory for the JournalOverviewViewModel.
         *
         * @param repository The repository to fetch journal entries from.
         * @return A ViewModelProvider.Factory for creating JournalOverviewViewModel instances.
         */
        fun factory(repository: JournalRepository): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    JournalOverviewViewModel(repository)
                }
            }
    }
}