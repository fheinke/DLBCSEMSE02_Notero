package com.fheinke.notero.ui.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.fheinke.notero.data.local.entities.JournalEntryEntity
import com.fheinke.notero.data.repository.JournalRepository
import com.fheinke.notero.data.validation.JournalEntryValidator
import com.fheinke.notero.data.validation.ValidationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * Represents the state of the entry editor form.
 *
 * @property title The title of the journal entry.
 * @property text The text content of the journal entry.
 * @property mood The mood rating associated with the journal entry.
 * @property sleepScore The sleep score associated with the journal entry.
 * @property stressLevel The stress level associated with the journal entry.
 * @property titleError Indicates whether there is an error with the title field.
 */
data class EntryEditorFormState(
    val title: String = "",
    val text: String = "",
    val mood: Int? = null,
    val sleepScore: Int? = null,
    val stressLevel: Int? = null,
    val titleError: Boolean = false
)

/**
 * ViewModel for managing the state and logic of the entry editor screen.
 *
 * @property repository The repository for accessing journal entries.
 * @property entryId The ID of the journal entry being edited (null if creating a new entry).
 */
class EntryEditorViewModel(
    private val repository: JournalRepository,
    private val entryId: Long?
) : ViewModel() {

    private val _formState = MutableStateFlow(EntryEditorFormState())
    val formState: StateFlow<EntryEditorFormState> = _formState

    private val _isSaved = MutableStateFlow(false)
    val isSaved: StateFlow<Boolean> = _isSaved

    init {
        if (entryId != null) loadEntry(entryId)
    }

    /**
     * Loads the journal entry from the repository and updates the form state.
     *
     * @param id The ID of the journal entry to load.
     */
    private fun loadEntry(id: Long) {
        viewModelScope.launch {
            val entry = repository.getById(id) ?: return@launch
            _formState.value = EntryEditorFormState(
                title = entry.title,
                text = entry.text,
                mood = entry.mood,
                sleepScore = entry.sleepScore,
                stressLevel = entry.stressLevel
            )
        }
    }

    /**
     * Handles changes to the title field.
     *
     * @param value The new value of the title field.
     */
    fun onTitleChange(value: String) {
        _formState.value = _formState.value.copy(title = value, titleError = false)
    }

    /**
     * Handles changes to the text field.
     *
     * @param value The new value of the text field.
     */
    fun onTextChange(value: String) {
        _formState.value = _formState.value.copy(text = value)
    }

    /**
     * Handles changes to the mood field.
     *
     * @param value The new value of the mood field.
     */
    fun onMoodChange(value: Int?) {
        _formState.value = _formState.value.copy(mood = value)
    }

    /**
     * Handles changes to the sleep score field.
     *
     * @param value The new value of the sleep score field.
     */
    fun onSleepScoreChange(value: Int?) {
        _formState.value = _formState.value.copy(sleepScore = value)
    }

    /**
     * Handles changes to the stress level field.
     *
     * @param value The new value of the stress level field.
     */
    fun onStressLevelChange(value: Int?) {
        _formState.value = _formState.value.copy(stressLevel = value)
    }

    /**
     * Saves the journal entry.
     */
    fun saveEntry() {
        val form = _formState.value

        // Check if both title and text are blank, and set titleError to true if they are
        if (form.title.isBlank() && form.text.isBlank()) {
            _formState.value = form.copy(titleError = true)
            return
        }

        // Validate the entry using JournalEntryValidator
        val validation = JournalEntryValidator.validate(
            title = form.title,
            text = form.text,
            mood = form.mood,
            sleepScore = form.sleepScore,
            stressLevel = form.stressLevel
        )
        if (validation is ValidationResult.Invalid) return

        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val entry = JournalEntryEntity(
                id = entryId ?: 0L,
                title = form.title.trim(),
                text = form.text.trim(),
                mood = form.mood,
                sleepScore = form.sleepScore,
                stressLevel = form.stressLevel,
                entryDate = LocalDate.now(),
                createdAt = now,
                updatedAt = now
            )
            repository.saveEntry(entry)
            _isSaved.value = true
        }
    }

    /**
     * Factory method for creating an instance of EntryEditorViewModel.
     */
    companion object {

        /**
         * Creates a ViewModelProvider.Factory for EntryEditorViewModel.
         *
         * @param repository The repository for accessing journal entries.
         * @param entryId The ID of the journal entry being edited (null if creating a new entry).
         * @return A ViewModelProvider.Factory for EntryEditorViewModel.
         */
        fun factory(repository: JournalRepository, entryId: Long?): ViewModelProvider.Factory =
            viewModelFactory {
                initializer { EntryEditorViewModel(repository, entryId) }
            }
    }
}