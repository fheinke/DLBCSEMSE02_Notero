package com.fheinke.notero

import com.fheinke.notero.data.validation.PeriodRecordValidator
import com.fheinke.notero.data.validation.ValidationResult
import org.junit.Test
import org.junit.Assert.*
import java.time.LocalDate

class PeriodRecordValidatorTest {
    @Test
    fun `valid period record with all fields`() {
        val result = PeriodRecordValidator.validate(
            startDate = LocalDate.of(2024, 1, 1),
            endDate = LocalDate.of(2024, 1, 5),
            flowIntensity = 3
        )
        assertTrue(result is ValidationResult.Valid)
    }

    @Test
    fun `valid period record without end date`() {
        val result = PeriodRecordValidator.validate(
            startDate = LocalDate.of(2024, 1, 1),
            endDate = null,
            flowIntensity = 3
        )
        assertTrue(result is ValidationResult.Valid)
    }

    @Test
    fun `valid period record without flow intensity`() {
        val result = PeriodRecordValidator.validate(
            startDate = LocalDate.of(2024, 1, 1),
            endDate = LocalDate.of(2024, 1, 5),
            flowIntensity = null
        )
        assertTrue(result is ValidationResult.Valid)
    }

    @Test
    fun `valid period record with same start and end date`() {
        val date = LocalDate.of(2024, 1, 1)
        val result = PeriodRecordValidator.validate(
            startDate = date,
            endDate = date,
            flowIntensity = 3
        )
        assertTrue(result is ValidationResult.Valid)
    }

    @Test
    fun `invalid period record with end date before start date`() {
        val result = PeriodRecordValidator.validate(
            startDate = LocalDate.of(2024, 1, 5),
            endDate = LocalDate.of(2024, 1, 1),
            flowIntensity = 3
        )
        assertTrue(result is ValidationResult.Invalid)
        assertEquals("End date cannot be before start date", (result as ValidationResult.Invalid).message)
    }

    @Test
    fun `invalid flow intensity too low`() {
        val result = PeriodRecordValidator.validate(
            startDate = LocalDate.of(2024, 1, 1),
            endDate = LocalDate.of(2024, 1, 5),
            flowIntensity = 0
        )
        assertTrue(result is ValidationResult.Invalid)
        assertEquals("Flow intensity must be between 1 and 5", (result as ValidationResult.Invalid).message)
    }

    @Test
    fun `invalid flow intensity too high`() {
        val result = PeriodRecordValidator.validate(
            startDate = LocalDate.of(2024, 1, 1),
            endDate = LocalDate.of(2024, 1, 5),
            flowIntensity = 6
        )
        assertTrue(result is ValidationResult.Invalid)
        assertEquals("Flow intensity must be between 1 and 5", (result as ValidationResult.Invalid).message)
    }

    @Test
    fun `valid boundary values for flow intensity`() {
        val result1 = PeriodRecordValidator.validate(
            LocalDate.of(2024, 1, 1),
            null,
            1
        )
        val result2 = PeriodRecordValidator.validate(
            LocalDate.of(2024, 1, 1),
            null,
            5
        )
        assertTrue(result1 is ValidationResult.Valid)
        assertTrue(result2 is ValidationResult.Valid)
    }

    @Test
    fun `invalid flow intensity negative value`() {
        val result = PeriodRecordValidator.validate(
            startDate = LocalDate.of(2024, 1, 1),
            endDate = null,
            flowIntensity = -1
        )
        assertTrue(result is ValidationResult.Invalid)
        assertEquals("Flow intensity must be between 1 and 5", (result as ValidationResult.Invalid).message)
    }
}