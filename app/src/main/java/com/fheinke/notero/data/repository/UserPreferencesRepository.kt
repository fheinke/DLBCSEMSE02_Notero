package com.fheinke.notero.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * Repository for managing user preferences using SharedPreferences.
 * Provides methods to get and set various user preferences such as onboarding completion status, user name, user gender, and period tracking settings.
 *
 * @see SharedPreferences
 */
class UserPreferencesRepository(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("notero_preferences", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_GENDER = "user_gender"
        private const val KEY_PERIOD_TRACKING_ENABLED = "period_tracking_enabled"
    }

    /**
     * Sets the onboarding completion status.
     *
     * @param completed True if onboarding is completed, false otherwise.
     */
    fun setOnboardingCompleted(completed: Boolean) {
        sharedPreferences.edit { putBoolean(KEY_ONBOARDING_COMPLETED, completed) }
    }

    /**
     * Checks if onboarding is completed.
     *
     * @return True if onboarding is completed, false otherwise.
     */
    fun isOnboardingCompleted(): Boolean {
        return sharedPreferences.getBoolean(KEY_ONBOARDING_COMPLETED, false)
    }

    /**
     * Sets the user's name.
     *
     * @param name The name of the user.
     */
    fun setUserName(name: String) {
        sharedPreferences.edit { putString(KEY_USER_NAME, name) }
    }

    /**
     * Retrieves the user's name.
     *
     * @return The name of the user, or null if not set.
     */
    fun getUserName(): String? {
        return sharedPreferences.getString(KEY_USER_NAME, null)
    }

    /**
     * Sets the user's gender.
     *
     * @param gender The gender of the user.
     */
    fun setUserGender(gender: String?) {
        sharedPreferences.edit { putString(KEY_USER_GENDER, gender) }
    }

    /**
     * Retrieves the user's gender.
     *
     * @return The gender of the user, or null if not set.
     */
    fun getUserGender(): String? {
        return sharedPreferences.getString(KEY_USER_GENDER, null)
    }

    /**
     * Sets whether period tracking is enabled.
     *
     * @param enabled True if period tracking is enabled, false otherwise.
     */
    fun setPeriodTrackingEnabled(enabled: Boolean) {
        sharedPreferences.edit { putBoolean(KEY_PERIOD_TRACKING_ENABLED, enabled) }
    }

    /**
     * Checks if period tracking is enabled.
     *
     * @return True if period tracking is enabled, false otherwise.
     */
    fun isPeriodTrackingEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_PERIOD_TRACKING_ENABLED, false)
    }
}