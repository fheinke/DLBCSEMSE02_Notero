package com.fheinke.notero.ui.settings

import com.fheinke.notero.data.repository.UserPreferencesRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel for the Settings screen.
 *
 * @property prefs The UserPreferencesRepository for accessing user preferences.
 * @returns A ViewModel instance for the Settings screen.
 */
class SettingsViewModel(
    private val prefs: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState

    init {
        viewModelScope.launch {
            _uiState.value = SettingsUiState(
                userName = prefs.userName.first() ?: "",
                gender = prefs.userGender.first(),
                periodTrackingEnabled = prefs.periodTrackingEnabled.first()
            )
        }
    }

    /**
     * Updates the userName in the UI state.
     *
     * @param value The new userName.
     */
    fun onUserNameChange(value: String) {
        _uiState.value = _uiState.value.copy(userName = value)
    }

    /**
     * Updates the gender in the UI state and handles period tracking logic.
     *
     * @param value The new gender value.
     */
    fun onGenderChange(value: String?) {
        _uiState.value = _uiState.value.copy(
            gender = value,
            periodTrackingEnabled = if (value != "female") false else _uiState.value.periodTrackingEnabled
        )
        viewModelScope.launch {
            prefs.setUserGender(value)
            if (value != "female") prefs.setPeriodTrackingEnabled(false)
        }
    }

    /**
     * Updates the period tracking enabled state in the UI state and persists the change.
     *
     * @param value The new period tracking enabled state.
     */
    fun onPeriodTrackingChange(value: Boolean) {
        _uiState.value = _uiState.value.copy(periodTrackingEnabled = value)
        viewModelScope.launch { prefs.setPeriodTrackingEnabled(value) }
    }

    /**
     * Saves the current userName to the preferences.
     */
    fun saveUserName() {
        val name = _uiState.value.userName.trim()
        viewModelScope.launch { prefs.setUserName(name) }
    }

    companion object {
        /**
         * Factory method to create a ViewModelProvider.Factory for SettingsViewModel.
         *
         * @param prefs The UserPreferencesRepository for accessing user preferences.
         * @returns A ViewModelProvider.Factory instance for creating SettingsViewModel.
         */
        fun factory(prefs: UserPreferencesRepository): ViewModelProvider.Factory =
            viewModelFactory {
                initializer { SettingsViewModel(prefs) }
            }
    }
}

/**
 * Data class representing the UI state for the Settings screen.
 *
 * @property userName The user's name.
 * @property gender The user's gender (nullable).
 * @property periodTrackingEnabled Whether period tracking is enabled.
 */
data class SettingsUiState(
    val userName: String = "",
    val gender: String? = null,
    val periodTrackingEnabled: Boolean = false
)
