package com.fheinke.notero.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class UserPreferencesRepository(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("notero_preferences", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_GENDER = "user_gender"
        private const val KEY_PERIOD_TRACKING_ENABLED = "period_tracking_enabled"
    }

    fun setOnboardingCompleted(completed: Boolean) {
        sharedPreferences.edit { putBoolean(KEY_ONBOARDING_COMPLETED, completed) }
    }

    fun isOnboardingCompleted(): Boolean {
        return sharedPreferences.getBoolean(KEY_ONBOARDING_COMPLETED, false)
    }

    fun setUserName(name: String) {
        sharedPreferences.edit { putString(KEY_USER_NAME, name) }
    }

    fun getUserName(): String? {
        return sharedPreferences.getString(KEY_USER_NAME, null)
    }

    fun setUserGender(gender: String?) {
        sharedPreferences.edit { putString(KEY_USER_GENDER, gender) }
    }

    fun getUserGender(): String? {
        return sharedPreferences.getString(KEY_USER_GENDER, null)
    }

    fun setPeriodTrackingEnabled(enabled: Boolean) {
        sharedPreferences.edit { putBoolean(KEY_PERIOD_TRACKING_ENABLED, enabled) }
    }

    fun isPeriodTrackingEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_PERIOD_TRACKING_ENABLED, false)
    }
}