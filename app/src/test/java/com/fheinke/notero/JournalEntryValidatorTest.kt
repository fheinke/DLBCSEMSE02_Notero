package com.fheinke.notero

import com.fheinke.notero.data.validation.JournalEntryValidator
import com.fheinke.notero.data.validation.ValidationResult
import org.junit.Test
import org.junit.Assert.*

class JournalEntryValidatorTest {
    @Test
    fun `valid entry with all fields`() {
        val result = JournalEntryValidator.validate(
            title = "Test Title",
            text = "Test Text",
            mood = 3,
            sleepScore = 4,
            stressLevel = 2
        )
        assertTrue(result is ValidationResult.Valid)
    }

    @Test
    fun `valid entry with only title`() {
        val result = JournalEntryValidator.validate(
            title = "Test Title",
            text = "",
            mood = null,
            sleepScore = null,
            stressLevel = null
        )
        assertTrue(result is ValidationResult.Valid)
    }

    @Test
    fun `valid entry with only text`() {
        val result = JournalEntryValidator.validate(
            title = "",
            text = "Test Text",
            mood = null,
            sleepScore = null,
            stressLevel = null
        )
        assertTrue(result is ValidationResult.Valid)
    }

    @Test
    fun `invalid entry with empty title and text`() {
        val result = JournalEntryValidator.validate(
            title = "",
            text = "",
            mood = null,
            sleepScore = null,
            stressLevel = null
        )
        assertTrue(result is ValidationResult.Invalid)
        assertEquals("Title and text cannot be empty", (result as ValidationResult.Invalid).message)
    }

    @Test
    fun `invalid entry with blank title and text`() {
        val result = JournalEntryValidator.validate(
            title = "   ",
            text = "   ",
            mood = null,
            sleepScore = null,
            stressLevel = null
        )
        assertTrue(result is ValidationResult.Invalid)
        assertEquals("Title and text cannot be empty", (result as ValidationResult.Invalid).message)
    }

    @Test
    fun `invalid mood too low`() {
        val result = JournalEntryValidator.validate(
            title = "Test",
            text = "",
            mood = 0,
            sleepScore = null,
            stressLevel = null
        )
        assertTrue(result is ValidationResult.Invalid)
        assertEquals("Mood must be between 1 and 5", (result as ValidationResult.Invalid).message)
    }

    @Test
    fun `invalid mood too high`() {
        val result = JournalEntryValidator.validate(
            title = "Test",
            text = "",
            mood = 6,
            sleepScore = null,
            stressLevel = null
        )
        assertTrue(result is ValidationResult.Invalid)
        assertEquals("Mood must be between 1 and 5", (result as ValidationResult.Invalid).message)
    }

    @Test
    fun `invalid sleep score too low`() {
        val result = JournalEntryValidator.validate(
            title = "Test",
            text = "",
            mood = null,
            sleepScore = 0,
            stressLevel = null
        )
        assertTrue(result is ValidationResult.Invalid)
        assertEquals("Sleep score must be between 1 and 5", (result as ValidationResult.Invalid).message)
    }

    @Test
    fun `invalid sleep score too high`() {
        val result = JournalEntryValidator.validate(
            title = "Test",
            text = "",
            mood = null,
            sleepScore = 6,
            stressLevel = null
        )
        assertTrue(result is ValidationResult.Invalid)
        assertEquals("Sleep score must be between 1 and 5", (result as ValidationResult.Invalid).message)
    }

    @Test
    fun `invalid stress level too low`() {
        val result = JournalEntryValidator.validate(
            title = "Test",
            text = "",
            mood = null,
            sleepScore = null,
            stressLevel = 0
        )
        assertTrue(result is ValidationResult.Invalid)
        assertEquals("Stress level must be between 1 and 5", (result as ValidationResult.Invalid).message)
    }

    @Test
    fun `invalid stress level too high`() {
        val result = JournalEntryValidator.validate(
            title = "Test",
            text = "",
            mood = null,
            sleepScore = null,
            stressLevel = 6
        )
        assertTrue(result is ValidationResult.Invalid)
        assertEquals("Stress level must be between 1 and 5", (result as ValidationResult.Invalid).message)
    }

    @Test
    fun `valid boundary values for mood`() {
        val result1 = JournalEntryValidator.validate("Test", "", 1, null, null)
        val result2 = JournalEntryValidator.validate("Test", "", 5, null, null)
        assertTrue(result1 is ValidationResult.Valid)
        assertTrue(result2 is ValidationResult.Valid)
    }

    @Test
    fun `valid boundary values for sleep score`() {
        val result1 = JournalEntryValidator.validate("Test", "", null, 1, null)
        val result2 = JournalEntryValidator.validate("Test", "", null, 5, null)
        assertTrue(result1 is ValidationResult.Valid)
        assertTrue(result2 is ValidationResult.Valid)
    }

    @Test
    fun `valid boundary values for stress level`() {
        val result1 = JournalEntryValidator.validate("Test", "", null, null, 1)
        val result2 = JournalEntryValidator.validate("Test", "", null, null, 5)
        assertTrue(result1 is ValidationResult.Valid)
        assertTrue(result2 is ValidationResult.Valid)
    }
}