package com.fheinke.notero.data.validation

import java.time.LocalDate

/**
 * Validates a period record with start date, optional end date, and optional flow intensity.
 */
object PeriodRecordValidator {

    /**
     * Validates the given period record parameters.
     *
     * @param startDate The start date of the period (must not be null).
     * @param endDate The optional end date of the period (can be null).
     * @param flowIntensity The optional flow intensity (can be null, must be between 1 and 5 if provided).
     * @return A ValidationResult indicating whether the input is valid or invalid with an error message.
     */
    fun validate(
        startDate: LocalDate,
        endDate: LocalDate?,
        flowIntensity: Int?
    ): ValidationResult {
        if (endDate != null && endDate.isBefore(startDate))
            return ValidationResult.Invalid("End date cannot be before start date")
        if (flowIntensity != null && flowIntensity !in 1..5)
            return ValidationResult.Invalid("Flow intensity must be between 1 and 5")

        return ValidationResult.Valid
    }
}