package com.fheinke.notero

import com.fheinke.notero.data.local.entities.JournalEntryEntity
import com.fheinke.notero.data.repository.JournalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import java.time.LocalDate

class FakeJournalRepository : JournalRepository {
    private val entries = mutableListOf<JournalEntryEntity>()
    private var nextId = 1L

    override fun getAll(): Flow<List<JournalEntryEntity>> = flowOf(entries.toList())

    override suspend fun getById(id: Long): JournalEntryEntity? = entries.find { it.id == id }

    override suspend fun saveEntry(entry: JournalEntryEntity) {
        val existing = entries.find { it.id == entry.id }
        if (existing != null) {
            entries.remove(existing)
            entries.add(entry)
        } else {
            entries.add(entry.copy(id = nextId++))
        }
    }

    override suspend fun deleteEntry(entry: JournalEntryEntity) {
        entries.remove(entry)
    }

    override suspend fun deleteEntryById(id: Long) {
        entries.removeIf { it.id == id }
    }

    fun clear() {
        entries.clear()
        nextId = 1L
    }
}

class JournalRepositoryTest {
    private val repository = FakeJournalRepository()

    @Before
    fun setup() {
        repository.clear()
    }

    @Test
    fun `create entry and retrieve by id`() = runTest {
        val entry = JournalEntryEntity(
            id = 0L,
            entryDate = LocalDate.of(2026, 9, 1),
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            title = "Tag der Tests",
            text = "Test text zum testen des Texts",
            mood = 5,
            sleepScore = 4,
            stressLevel = 2
        )

        repository.saveEntry(entry)
        val retrieved = repository.getById(1L)

        assertNotNull(retrieved)
        assertEquals(LocalDate.of(2026, 9, 1), retrieved?.entryDate)
        assertEquals(entry.createdAt, retrieved?.createdAt)
        assertEquals(entry.updatedAt, retrieved?.updatedAt)
        assertEquals("Tag der Tests", retrieved?.title)
        assertEquals("Test text zum testen des Texts", retrieved?.text)
        assertEquals(5, retrieved?.mood)
        assertEquals(4, retrieved?.sleepScore)
        assertEquals(2, retrieved?.stressLevel)
    }

    @Test
    fun `delete entry by id removes entry`() = runTest {
        val entry = JournalEntryEntity(
            id = 0L,
            entryDate = LocalDate.of(2026, 9, 1),
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            title = "Test",
            text = "Text",
            mood = null,
            sleepScore = null,
            stressLevel = null
        )

        repository.saveEntry(entry)
        repository.deleteEntryById(1L)
        val retrieved = repository.getById(1L)

        assertNull(retrieved)
    }

    @Test
    fun `delete entry removes entry`() = runTest {
        val entry = JournalEntryEntity(
            id = 0L,
            entryDate = LocalDate.of(2026, 9, 1),
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            title = "Test",
            text = "Text",
            mood = null,
            sleepScore = null,
            stressLevel = null
        )

        repository.saveEntry(entry)
        repository.deleteEntry(entry.copy(id = 1L))
        val retrieved = repository.getById(1L)

        assertNull(retrieved)
    }

    @Test
    fun `update existing entry`() = runTest {
        val entry = JournalEntryEntity(
            id = 0L,
            entryDate = LocalDate.of(2026, 9, 1),
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            title = "Original Title",
            text = "Original Text",
            mood = 3,
            sleepScore = 4,
            stressLevel = 2
        )

        repository.saveEntry(entry)
        
        val updatedEntry = entry.copy(
            id = 1L,
            title = "Updated Title",
            text = "Updated Text",
            updatedAt = System.currentTimeMillis()
        )
        repository.saveEntry(updatedEntry)

        val retrieved = repository.getById(1L)
        assertEquals("Updated Title", retrieved?.title)
        assertEquals("Updated Text", retrieved?.text)
    }

    @Test
    fun `get non-existent entry returns null`() = runTest {
        val retrieved = repository.getById(999L)
        assertNull(retrieved)
    }

    @Test
    fun `get all entries returns correct list`() = runTest {
        val entry1 = JournalEntryEntity(
            id = 0L,
            entryDate = LocalDate.of(2026, 9, 1),
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            title = "Entry 1",
            text = "Text 1",
            mood = null,
            sleepScore = null,
            stressLevel = null
        )

        val entry2 = JournalEntryEntity(
            id = 0L,
            entryDate = LocalDate.of(2026, 9, 2),
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            title = "Entry 2",
            text = "Text 2",
            mood = null,
            sleepScore = null,
            stressLevel = null
        )

        repository.saveEntry(entry1)
        repository.saveEntry(entry2)

        val allEntries = repository.getAll().single()
        assertEquals(2, allEntries.size)
        assertTrue(allEntries.any { it.title == "Entry 1" })
        assertTrue(allEntries.any { it.title == "Entry 2" })
    }

    @Test
    fun `clear repository removes all entries`() = runTest {
        val entry = JournalEntryEntity(
            id = 0L,
            entryDate = LocalDate.of(2026, 9, 1),
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            title = "Test",
            text = "Text",
            mood = null,
            sleepScore = null,
            stressLevel = null
        )

        repository.saveEntry(entry)
        repository.clear()
        val allEntries = repository.getAll().single()

        assertTrue(allEntries.isEmpty())
    }

    @Test
    fun `save entry with null fields`() = runTest {
        val entry = JournalEntryEntity(
            id = 0L,
            entryDate = LocalDate.of(2026, 9, 1),
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            title = "Test",
            text = "Text",
            mood = null,
            sleepScore = null,
            stressLevel = null
        )

        repository.saveEntry(entry)
        val retrieved = repository.getById(1L)

        assertNotNull(retrieved)
        assertNull(retrieved?.mood)
        assertNull(retrieved?.sleepScore)
        assertNull(retrieved?.stressLevel)
    }
}