package com.fheinke.notero.ui.period

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.fheinke.notero.data.local.entities.PeriodRecordEntity
import com.fheinke.notero.data.repository.PeriodRecordRepository
import com.fheinke.notero.data.validation.PeriodRecordValidator
import com.fheinke.notero.data.validation.ValidationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * Represents the state of the period editor form.
 *
 * @property startDate The start date of the period.
 * @property endDate The end date of the period (nullable).
 * @property flowIntensity The intensity of the flow (1-5).
 * @property note An optional note associated with the period.
 * @property startDateError Indicates if there is an error with the start date.
 * @property endDateError Indicates if there is an error with the end date.
 */
data class PeriodEditorFormState(
    val startDate: LocalDate = LocalDate.now(),
    val endDate: LocalDate? = null,
    val flowIntensity: Int = 3,
    val note: String = "",
    val startDateError: Boolean = false,
    val endDateError: Boolean = false
)

 /**
  * ViewModel for the PeriodEditorScreen.
  * It manages the state of the form and handles saving the period record.
  *
  * @param repository The repository for accessing period records.
  * @param recordId The ID of the record being edited (nullable).
  * @returns A ViewModel instance for the PeriodEditorScreen.
 */
class PeriodEditorViewModel(
    private val repository: PeriodRecordRepository,
    private val recordId: Long?
) : ViewModel() {

    private val _formState = MutableStateFlow(PeriodEditorFormState())
    val formState: StateFlow<PeriodEditorFormState> = _formState

    private val _isSaved = MutableStateFlow(false)
    val isSaved: StateFlow<Boolean> = _isSaved

    init {
        if (recordId != null) loadRecord(recordId)
    }

    /**
     * Loads a period record by its ID and updates the form state.
     *
     * @param id The ID of the period record to load.
     */
    private fun loadRecord(id: Long) {
        viewModelScope.launch {
            val record = repository.getById(id) ?: return@launch
            _formState.value = PeriodEditorFormState(
                startDate = record.startDate,
                endDate = record.endDate,
                flowIntensity = record.flowIntensity ?: 3,
                note = record.note ?: ""
            )
        }
    }

    /**
     * Updates the start date in the form state and validates the dates.
     *
     * @param date The new start date.
     */
    fun onStartDateChange(date: LocalDate) {
        val endDate = _formState.value.endDate
        _formState.value = _formState.value.copy(
            startDate = date,
            startDateError = false,
            endDateError = endDate != null && endDate.isBefore(date)
        )
    }

    /**
     * Updates the end date in the form state and validates the dates.
     *
     * @param date The new end date (nullable).
     */
    fun onEndDateChange(date: LocalDate?) {
        _formState.value = _formState.value.copy(
            endDate = date,
            endDateError = date != null && date.isBefore(_formState.value.startDate)
        )
    }

    /**
     * Updates the flow intensity in the form state.
     *
     * @param value The new flow intensity.
     */
    fun onFlowIntensityChange(value: Int) {
        _formState.value = _formState.value.copy(flowIntensity = value)
    }

    /**
     * Updates the note in the form state.
     *
     * @param value The new note.
     */
    fun onNoteChange(value: String) {
        _formState.value = _formState.value.copy(note = value)
    }

    /**
     * Saves the period record.
     */
    fun saveRecord() {
        val form = _formState.value

        // Validate the form state before saving
        if (form.startDateError || form.endDateError) {
            _formState.value = form.copy(
                startDateError = form.startDateError,
                endDateError = form.endDateError
            )
            return
        }

        // Validate the period record using the validator
        val validation = PeriodRecordValidator.validate(form.startDate, form.endDate, form.flowIntensity)
        if (validation is ValidationResult.Invalid) return

        viewModelScope.launch {
            val now = System.currentTimeMillis()
            repository.saveEntry(
                PeriodRecordEntity(
                    id = recordId ?: 0L,
                    startDate = form.startDate,
                    endDate = form.endDate,
                    flowIntensity = form.flowIntensity,
                    note = form.note.trim().ifBlank { null },
                    createdAt = now,
                    updatedAt = now
                )
            )
            _isSaved.value = true
        }
    }

    companion object {
        /**
         * Factory method to create a ViewModelProvider.Factory for PeriodEditorViewModel.
         *
         * @param repository The repository for accessing period records.
         * @param recordId The ID of the record being edited (nullable).
         * @return A ViewModelProvider.Factory instance for creating PeriodEditorViewModel.
         */
        fun factory(repository: PeriodRecordRepository, recordId: Long?): ViewModelProvider.Factory =
            viewModelFactory {
                initializer { PeriodEditorViewModel(repository, recordId) }
            }
    }
}