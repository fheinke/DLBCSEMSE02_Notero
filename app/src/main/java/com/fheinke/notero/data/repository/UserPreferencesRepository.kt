package com.fheinke.notero.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val PREFERENCES_NAME = "user_preferences"

/**
 * Extension property to provide a DataStore instance for the Context.
 * This allows easy access to the DataStore for user preferences.
 */
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = PREFERENCES_NAME,
)

/**
 * Repository for managing user preferences using DataStore.
 * Provides flows and suspend setters for onboarding completion status, user name, user gender, and period tracking settings.
 *
 * @see DataStore
 */
class UserPreferencesRepository(context: Context) {
    private val dataStore: DataStore<Preferences> = context.applicationContext.dataStore

    private object Keys {
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_GENDER = stringPreferencesKey("user_gender")
        val PERIOD_TRACKING_ENABLED = booleanPreferencesKey("period_tracking_enabled")
    }

    /** Emits whether onboarding is completed. */
    val onboardingCompleted: Flow<Boolean> =
        dataStore.data.map { it[Keys.ONBOARDING_COMPLETED] ?: false }

    /** Emits the user's name, or null if not set. */
    val userName: Flow<String?> = dataStore.data.map { it[Keys.USER_NAME] }

    /** Emits the user's gender, or null if not set. */
    val userGender: Flow<String?> = dataStore.data.map { it[Keys.USER_GENDER] }

    /** Emits whether period tracking is enabled. */
    val periodTrackingEnabled: Flow<Boolean> =
        dataStore.data.map { it[Keys.PERIOD_TRACKING_ENABLED] ?: false }

    /**
     * Sets the user's name.
     *
     * @param name The name of the user.
     */
    suspend fun setUserName(name: String) {
        dataStore.edit { it[Keys.USER_NAME] = name }
    }

    /**
     * Sets the user's gender. A null value removes the stored gender.
     *
     * @param gender The gender of the user.
     */
    suspend fun setUserGender(gender: String?) {
        dataStore.edit {
            if (gender == null) it.remove(Keys.USER_GENDER) else it[Keys.USER_GENDER] = gender
        }
    }

    /**
     * Sets whether period tracking is enabled.
     *
     * @param enabled True if period tracking is enabled, false otherwise.
     */
    suspend fun setPeriodTrackingEnabled(enabled: Boolean) {
        dataStore.edit { it[Keys.PERIOD_TRACKING_ENABLED] = enabled }
    }

    /**
     * Atomically stores all onboarding results and marks onboarding as completed.
     *
     * @param name The user's name.
     * @param gender The user's gender (can be null).
     * @param periodTracking Whether period tracking is enabled.
     */
    suspend fun completeOnboarding(name: String, gender: String?, periodTracking: Boolean) {
        dataStore.edit {
            it[Keys.USER_NAME] = name
            if (gender == null) it.remove(Keys.USER_GENDER) else it[Keys.USER_GENDER] = gender
            it[Keys.PERIOD_TRACKING_ENABLED] = periodTracking
            it[Keys.ONBOARDING_COMPLETED] = true
        }
    }
}
