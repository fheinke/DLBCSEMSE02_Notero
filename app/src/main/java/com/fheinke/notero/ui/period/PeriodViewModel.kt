package com.fheinke.notero.ui.period

import com.fheinke.notero.data.local.entities.PeriodRecordEntity
import com.fheinke.notero.data.repository.PeriodRecordRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * Represents the UI state for the period tracking feature.
 *
 * Loading: Indicates that the data is currently being loaded.
 * Empty: Indicates that there are no period records available.
 * Success: Indicates that period records have been successfully loaded, containing the list of records and the active record if any.
 */
sealed class PeriodUiState {
    object Loading : PeriodUiState()
    object Empty : PeriodUiState()
    data class Success(
        val records: List<PeriodRecordEntity>,
        val activeRecord: PeriodRecordEntity?
    ) : PeriodUiState()
}

/**
 * ViewModel for managing the state and operations related to period tracking.
 *
 * @property repository The repository responsible for handling period record data.
 * @returns A StateFlow representing the current UI state of the period tracking feature.
 */
class PeriodViewModel(
    private val repository: PeriodRecordRepository
) : ViewModel() {

    val uiState: StateFlow<PeriodUiState> = repository
        .getAll()
        .map { records ->
            if (records.isEmpty()) PeriodUiState.Empty
            else PeriodUiState.Success(
                records = records,
                activeRecord = records.firstOrNull { it.endDate == null }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PeriodUiState.Loading
        )

    /**
     * Starts a new period record with the specified flow intensity.
     *
     * @param flowIntensity The intensity of the flow for the new period record.
     */
    fun startPeriod(flowIntensity: Int?) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            repository.saveEntry(
                PeriodRecordEntity(
                    startDate = LocalDate.now(),
                    endDate = null,
                    flowIntensity = flowIntensity,
                    createdAt = now,
                    updatedAt = now
                )
            )
        }
    }

    /**
     * Ends the specified active period record.
     *
     * @param active The active period record to be ended.
     */
    fun endPeriod(active: PeriodRecordEntity) {
        viewModelScope.launch {
            repository.saveEntry(
                active.copy(endDate = LocalDate.now(), updatedAt = System.currentTimeMillis())
            )
        }
    }

    /**
     * Deletes the period record with the specified ID.
     *
     * @param id The ID of the period record to be deleted.
     */
    fun deleteRecord(id: Long) {
        viewModelScope.launch { repository.deleteEntryById(id) }
    }

    companion object {
        /**
         * Factory method for creating a ViewModelProvider.Factory for the PeriodViewModel.
         *
         * @param repository The repository responsible for handling period record data.
         * @returns A ViewModelProvider.Factory for creating instances of PeriodViewModel.
         */
        fun factory(repository: PeriodRecordRepository): ViewModelProvider.Factory =
            viewModelFactory {
                initializer { PeriodViewModel(repository) }
            }
    }
}