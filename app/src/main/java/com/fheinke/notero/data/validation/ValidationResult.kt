package com.fheinke.notero.data.validation

/**
 * Represents the result of a validation operation.
 */
sealed class ValidationResult {
    object Valid : ValidationResult()
    data class Invalid(val message: String) : ValidationResult()
}