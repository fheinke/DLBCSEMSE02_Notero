package com.fheinke.notero.data.validation

/**
 * Validates the input for a journal entry.
 */
object JournalEntryValidator {

    /**
     * Validates the input for a journal entry.
     *
     * @param title The title of the journal entry.
     * @param text The text of the journal entry.
     * @param mood The mood score of the journal entry (1-5).
     * @param sleepScore The sleep score of the journal entry (1-5).
     * @param stressLevel The stress level of the journal entry (1-5).
     * @return A [ValidationResult] indicating whether the input is valid or not.
     */
    fun validate(
        title: String,
        text: String,
        mood: Int?,
        sleepScore: Int?,
        stressLevel: Int?
    ): ValidationResult {
        if (title.isBlank() && text.isBlank()) {
            return ValidationResult.Invalid("Title and text cannot be empty")
        }
        if (mood != null && mood !in 1..5) {
            return ValidationResult.Invalid("Mood must be between 1 and 5")
        }
        if (sleepScore != null && sleepScore !in 1..5) {
            return ValidationResult.Invalid("Sleep score must be between 1 and 5")
        }
        if (stressLevel != null && stressLevel !in 1..5) {
            return ValidationResult.Invalid("Stress level must be between 1 and 5")
        }

        return ValidationResult.Valid
    }
}