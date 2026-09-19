package com.fheinke.notero.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.fheinke.notero.data.local.entities.JournalEntryEntity
import com.fheinke.notero.data.repository.JournalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Represents the UI state for the entry detail screen.
 */
sealed class EntryDetailUiState {
    object Loading : EntryDetailUiState()
    object NotFound : EntryDetailUiState()
    data class Success(val entry: JournalEntryEntity) : EntryDetailUiState()
}

/**
 * ViewModel for the entry detail screen.
 *
 * @property repository The repository to access journal entries.
 * @property entryId The ID of the journal entry to display.
 */
class EntryDetailViewModel(
    private val repository: JournalRepository,
    private val entryId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow<EntryDetailUiState>(EntryDetailUiState.Loading)
    val uiState: StateFlow<EntryDetailUiState> = _uiState

    init {
        loadEntry()
    }

    /**
     * Loads the journal entry from the repository and updates the UI state accordingly.
     */
    private fun loadEntry() {
        viewModelScope.launch {
            val entry = repository.getById(entryId)
            _uiState.value = if (entry != null) {
                EntryDetailUiState.Success(entry)
            } else {
                EntryDetailUiState.NotFound
            }
        }
    }

    /**
     * Deletes the journal entry and invokes the callback upon completion.
     *
     * @param onDeleted Callback function to be invoked after the entry is deleted.
     */
    fun deleteEntry(onDeleted: () -> Unit) {
        viewModelScope.launch {
            repository.deleteEntryById(entryId)
            onDeleted()
        }
    }

    companion object {

        /**
         * Creates a ViewModelProvider.Factory for EntryDetailViewModel.
         *
         * @param repository The repository to access journal entries.
         * @param entryId The ID of the journal entry to display.
         * @return A ViewModelProvider.Factory for creating EntryDetailViewModel instances.
         */
        fun factory(repository: JournalRepository, entryId: Long): ViewModelProvider.Factory =
            viewModelFactory {
                initializer { EntryDetailViewModel(repository, entryId) }
            }
    }
}