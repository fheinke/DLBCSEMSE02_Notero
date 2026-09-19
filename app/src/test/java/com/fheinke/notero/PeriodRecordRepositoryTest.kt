package com.fheinke.notero

import com.fheinke.notero.data.local.entities.PeriodRecordEntity
import com.fheinke.notero.data.repository.PeriodRecordRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class FakePeriodRecordRepository : PeriodRecordRepository {
    private val entries = mutableListOf<PeriodRecordEntity>()
    private var nextId = 1L

    override fun getAll(): Flow<List<PeriodRecordEntity>> = flowOf(entries.toList())

    override suspend fun getById(id: Long): PeriodRecordEntity? = entries.find { it.id == id }

    override suspend fun saveEntry(entry: PeriodRecordEntity) {
        val existing = entries.find { it.id == entry.id }
        if (existing != null) {
            entries.remove(existing)
            entries.add(entry)
        } else {
            entries.add(entry.copy(id = nextId++))
        }
    }

    override suspend fun deleteEntry(entry: PeriodRecordEntity) {
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

class PeriodRecordRepositoryTest {
    private val repository = FakePeriodRecordRepository()

    @Before
    fun setup() {
        repository.clear()
    }

    @Test
    fun `create entry and retrieve by id`() = runTest {
        val entry = PeriodRecordEntity(
            id = 0L,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            startDate = LocalDate.of(2026, 9, 15),
            endDate = LocalDate.of(2026, 9, 20),
            flowIntensity = 3,
            note = "Feeling a bit tired and bloated."
        )

        repository.saveEntry(entry)
        val savedEntry = repository.getById(1L)

        assertNotNull(savedEntry)
        assertEquals(entry.createdAt, savedEntry?.createdAt)
        assertEquals(entry.updatedAt, savedEntry?.updatedAt)
        assertEquals(LocalDate.of(2026, 9, 15), savedEntry?.startDate)
        assertEquals(LocalDate.of(2026, 9, 20), savedEntry?.endDate)
        assertEquals(3, savedEntry?.flowIntensity)
        assertEquals("Feeling a bit tired and bloated.", savedEntry?.note)
    }

    @Test
    fun `delete entry by id removes entry`() = runTest {
        val entry = PeriodRecordEntity(
            id = 0L,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            startDate = LocalDate.of(2026, 9, 15),
            endDate = LocalDate.of(2026, 9, 20),
            flowIntensity = 3,
            note = "Feeling a bit tired and bloated."
        )

        repository.saveEntry(entry)
        repository.deleteEntryById(1L)
        val deletedEntry = repository.getById(1L)

        assertEquals(null, deletedEntry)
    }

    @Test
    fun `delete entry by id removes the correct entry`() = runTest {
        val entry1 = PeriodRecordEntity(
            id = 0L,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            startDate = LocalDate.of(2026, 10, 1),
            endDate = LocalDate.of(2026, 10, 5),
            flowIntensity = 2,
            note = "Feeling normal."
        )
        val entry2 = PeriodRecordEntity(
            id = 0L,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            startDate = LocalDate.of(2026, 10, 6),
            endDate = LocalDate.of(2026, 10, 10),
            flowIntensity = 3,
            note = "Feeling good."
        )
        repository.saveEntry(entry1)
        repository.saveEntry(entry2)

        repository.deleteEntryById(1L)

        val allEntriesAfterDelete = repository.getAll().single()
        assertEquals(1, allEntriesAfterDelete.size)
        assertEquals("Feeling good.", allEntriesAfterDelete[0].note)
    }

    @Test
    fun `update entry and verify changes`() = runTest {
        val entry = PeriodRecordEntity(
            id = 0L,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            startDate = LocalDate.of(2026, 9, 15),
            endDate = LocalDate.of(2026, 9, 20),
            flowIntensity = 3,
            note = "Feeling a bit tired and bloated."
        )

        repository.saveEntry(entry)

        val updatedEntry = entry.copy(
            id = 1L,
            updatedAt = System.currentTimeMillis(),
            flowIntensity = 4,
            note = "Feeling better now after resting."
        )
        repository.saveEntry(updatedEntry)
        val retrievedEntry = repository.getById(1L)

        assertNotNull(retrievedEntry)
        assertEquals(4, retrievedEntry?.flowIntensity)
        assertEquals("Feeling better now after resting.", retrievedEntry?.note)
    }

    @Test
    fun `retrieve all entries returns correct list`() = runTest {
        val entry1 = PeriodRecordEntity(
            id = 0L,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            startDate = LocalDate.of(2026, 9, 15),
            endDate = LocalDate.of(2026, 9, 20),
            flowIntensity = 3,
            note = "Feeling a bit tired and bloated."
        )
        val entry2 = PeriodRecordEntity(
            id = 0L,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            startDate = LocalDate.of(2026, 10, 1),
            endDate = LocalDate.of(2026, 10, 5),
            flowIntensity = 2,
            note = "Feeling normal."
        )

        repository.saveEntry(entry1)
        repository.saveEntry(entry2)

        val allEntriesFromRepository = repository.getAll().single()
        val allEntriesFromTest = listOf(entry1.copy(id = 1L), entry2.copy(id = 2L))

        assertEquals(2, allEntriesFromRepository.size)
        assertEquals("Feeling a bit tired and bloated.", allEntriesFromRepository[0].note)
        assertEquals("Feeling normal.", allEntriesFromRepository[1].note)
        assertEquals(allEntriesFromTest, allEntriesFromRepository)
    }

    @Test
    fun `clear repository removes all entries`() = runTest {
        val entry1 = PeriodRecordEntity(
            id = 0L,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            startDate = LocalDate.of(2026, 9, 15),
            endDate = LocalDate.of(2026, 9, 20),
            flowIntensity = 3,
            note = "Feeling a bit tired and bloated."
        )
        val entry2 = PeriodRecordEntity(
            id = 0L,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            startDate = LocalDate.of(2026, 10, 1),
            endDate = LocalDate.of(2026, 10, 5),
            flowIntensity = 2,
            note = "Feeling normal."
        )
        repository.saveEntry(entry1)
        repository.saveEntry(entry2)

        val allEntriesFromRepositoryBeforeDelete = repository.getAll().single()
        assertEquals(2, allEntriesFromRepositoryBeforeDelete.size)

        repository.clear()

        val allEntriesFromRepository = repository.getAll().single()
        assertEquals(0, allEntriesFromRepository.size)
    }

    @Test
    fun `attempt to retrieve non-existent entry returns null`() = runTest {
        val retrievedEntry = repository.getById(999L)
        assertEquals(null, retrievedEntry)
    }

    @Test
    fun `saving multiple entries assigns unique IDs`() = runTest {
        val entry1 = PeriodRecordEntity(
            id = 0L,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            startDate = LocalDate.of(2026, 10, 1),
            endDate = LocalDate.of(2026, 10, 5),
            flowIntensity = 2,
            note = "Feeling normal."
        )
        val entry2 = PeriodRecordEntity(
            id = 0L,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            startDate = LocalDate.of(2026, 10, 6),
            endDate = LocalDate.of(2026, 10, 10),
            flowIntensity = 3,
            note = "Feeling good."
        )
        repository.saveEntry(entry1)
        repository.saveEntry(entry2)

        val allEntries = repository.getAll().single()
        assertEquals(2, allEntries.size)
        assertNotEquals(allEntries[0].id, allEntries[1].id)
    }

    @Test
    fun `update entry with duplicate ID updates existing entry`() = runTest {
        val entry = PeriodRecordEntity(
            id = 0L,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            startDate = LocalDate.of(2026, 9, 15),
            endDate = LocalDate.of(2026, 9, 20),
            flowIntensity = 3,
            note = "Feeling a bit tired and bloated."
        )

        repository.saveEntry(entry)

        val updatedEntry = entry.copy(
            id = 1L,
            updatedAt = System.currentTimeMillis(),
            flowIntensity = 4,
            note = "Feeling better now after resting."
        )
        repository.saveEntry(updatedEntry)
        val retrievedEntry = repository.getById(1L)

        assertNotNull(retrievedEntry)
        assertEquals(4, retrievedEntry?.flowIntensity)
        assertEquals("Feeling better now after resting.", retrievedEntry?.note)
    }

    @Test
    fun `update entry with non-existent ID creates new entry`() = runTest {
        val entry = PeriodRecordEntity(
            id = 999L,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            startDate = LocalDate.of(2026, 9, 15),
            endDate = LocalDate.of(2026, 9, 20),
            flowIntensity = 3,
            note = "Feeling a bit tired and bloated."
        )

        repository.saveEntry(entry)
        val retrievedEntry = repository.getById(1L)

        assertNotNull(retrievedEntry)
        assertEquals(1L, retrievedEntry?.id)
        assertEquals("Feeling a bit tired and bloated.", retrievedEntry?.note)
    }

    @Test
    fun `update entry with null note updates existing entry`() = runTest {
        val entry = PeriodRecordEntity(
            id = 0L,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            startDate = LocalDate.of(2026, 9, 15),
            endDate = LocalDate.of(2026, 9, 20),
            flowIntensity = 3,
            note = "Feeling a bit tired and bloated."
        )

        repository.saveEntry(entry)

        val updatedEntry = entry.copy(
            id = 1L,
            updatedAt = System.currentTimeMillis(),
            flowIntensity = 4,
            note = null
        )
        repository.saveEntry(updatedEntry)
        val retrievedEntry = repository.getById(1L)

        assertNotNull(retrievedEntry)
        assertEquals(4, retrievedEntry?.flowIntensity)
        assertEquals(null, retrievedEntry?.note)
    }

    @Test
    fun `update entry with null end date updates existing entry`() = runTest {
        val entry = PeriodRecordEntity(
            id = 0L,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            startDate = LocalDate.of(2026, 9, 15),
            endDate = LocalDate.of(2026, 9, 20),
            flowIntensity = 3,
            note = "Feeling a bit tired and bloated."
        )

        repository.saveEntry(entry)

        val updatedEntry = entry.copy(
            id = 1L,
            updatedAt = System.currentTimeMillis(),
            endDate = null
        )
        repository.saveEntry(updatedEntry)
        val retrievedEntry = repository.getById(1L)

        assertNotNull(retrievedEntry)
        assertEquals(null, retrievedEntry?.endDate)
    }

    @Test
    fun `create entry with end date before start date is handled correctly`() = runTest {
        val entry = PeriodRecordEntity(
            id = 0L,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            startDate = LocalDate.of(2026, 9, 20),
            endDate = LocalDate.of(2026, 9, 15),
            flowIntensity = 3,
            note = "Invalid date range."
        )

        repository.saveEntry(entry)
        val savedEntry = repository.getById(1L)

        assertNotNull(savedEntry)
        assertEquals(LocalDate.of(2026, 9, 20), savedEntry?.startDate)
        assertEquals(LocalDate.of(2026, 9, 15), savedEntry?.endDate)
    }

    @Test
    fun `create entry with flow intensity outside expected range is handled correctly`() = runTest {
        val entry = PeriodRecordEntity(
            id = 0L,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            startDate = LocalDate.of(2026, 9, 15),
            endDate = LocalDate.of(2026, 9, 20),
            flowIntensity = 6,
            note = "Flow intensity too high."
        )

        repository.saveEntry(entry)
        val savedEntry = repository.getById(1L)

        assertNotNull(savedEntry)
        assertEquals(6, savedEntry?.flowIntensity)
    }

    @Test
    fun `create entry with negative flow intensity is handled correctly`() = runTest {
        val entry = PeriodRecordEntity(
            id = 0L,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            startDate = LocalDate.of(2026, 9, 15),
            endDate = LocalDate.of(2026, 9, 20),
            flowIntensity = -1,
            note = "Flow intensity too low."
        )

        repository.saveEntry(entry)
        val savedEntry = repository.getById(1L)

        assertNotNull(savedEntry)
        assertEquals(-1, savedEntry?.flowIntensity)
    }
}