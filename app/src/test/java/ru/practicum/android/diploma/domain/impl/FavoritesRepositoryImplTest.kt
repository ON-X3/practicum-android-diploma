package ru.practicum.android.diploma.domain.impl

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.practicum.android.diploma.data.converters.VacancyCardConverter
import ru.practicum.android.diploma.data.converters.VacancyDetailConverter
import ru.practicum.android.diploma.data.db.dao.VacancyDetailDao
import ru.practicum.android.diploma.data.db.entity.VacancyDetailEntity
import ru.practicum.android.diploma.data.repository.FavoritesRepositoryImpl
import ru.practicum.android.diploma.domain.models.Employer
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.domain.models.VacancySalary

class FavoritesRepositoryImplTest {

    private val dao: VacancyDetailDao = mockk()
    private val detailConverter = VacancyDetailConverter()
    private val cardConverter = VacancyCardConverter()

    private val repository = FavoritesRepositoryImpl(
        vacancyDetailDao = dao,
        vacancyDetailConverter = detailConverter,
        vacancyCardConverter = cardConverter,
    )

    // ── addToFavorite ────────────────────────────────────────

    @Test
    fun `addToFavorite converts to entity and inserts`() = runTest {
        val vacancy = createVacancy()
        val entitySlot = slot<VacancyDetailEntity>()
        coEvery { dao.insertVacancy(capture(entitySlot)) } returns Unit

        repository.addToFavorite(vacancy)

        coVerify(exactly = 1) { dao.insertVacancy(any()) }
        assertEquals(vacancy.id, entitySlot.captured.id)
        assertEquals(vacancy.name, entitySlot.captured.name)
        assertEquals(vacancy.salary?.from, entitySlot.captured.salaryFrom)
    }

    // ── deleteFromFavoriteById ───────────────────────────────

    @Test
    fun `deleteFromFavoriteById calls dao`() = runTest {
        coEvery { dao.deleteVacancyById("1") } returns Unit

        repository.deleteFromFavoriteById("1")

        coVerify(exactly = 1) { dao.deleteVacancyById("1") }
    }

    // ── isFavorite ───────────────────────────────────────────

    @Test
    fun `isFavorite returns true when dao says true`() = runTest {
        coEvery { dao.isFavorite("1") } returns true

        val result = repository.isFavorite("1")

        assertTrue(result)
    }

    @Test
    fun `isFavorite returns false when dao says false`() = runTest {
        coEvery { dao.isFavorite("1") } returns false

        val result = repository.isFavorite("1")

        assertEquals(false, result)
    }

    // ── getVacancyById ───────────────────────────────────────

    @Test
    fun `getVacancyById returns domain when entity found`() = runTest {
        coEvery { dao.getVacancyById("1") } returns createEntity()

        val result = repository.getVacancyById("1")

        assertEquals("1", result?.id)
        assertEquals("Разработчик", result?.name)
    }

    // ── getFavoriteList ──────────────────────────────────────

    @Test
    fun `getFavoriteList maps entities to cards`() = runTest {
        val entities = listOf(createEntity(), createEntity().copy(id = "2"))
        coEvery { dao.getVacancyList() } returns flowOf(entities)

        repository.getFavoriteList().test {
            val list = awaitItem()
            assertEquals(2, list.size)
            assertEquals("1", list[0].id)
            assertEquals("2", list[1].id)
            awaitComplete()
        }
    }

    @Test
    fun `getFavoriteList returns empty list when no entities`() = runTest {
        coEvery { dao.getVacancyList() } returns flowOf(emptyList())

        repository.getFavoriteList().test {
            val list = awaitItem()
            assertEquals(emptyList<Any>(), list)
            awaitComplete()
        }
    }

    // ── helpers ──────────────────────────────────────────────

    private fun createVacancy() = VacancyDetail(
        id = "1",
        name = "Разработчик",
        descriptionHtml = "<p>Описание</p>",
        salary = VacancySalary(100000, 200000, "RUB"),
        address = "Москва",
        experience = "1-3 года",
        schedule = "Полный день",
        employment = "Полная занятость",
        employer = Employer("Яндекс", "https://logo.png"),
        areaName = "Москва",
        skills = listOf("Kotlin", "Android"),
        url = "https://hh.ru/vacancy/1",
        industryName = "Информационные технологии, системная интеграция, интернет",
        isFavorite = true
    )

    private fun createEntity() = VacancyDetailEntity(
        id = "1",
        name = "Разработчик",
        description = "<p>Описание</p>",
        url = "https://hh.ru/vacancy/1",
        salaryFrom = 100000,
        salaryTo = 200000,
        salaryCurrency = "RUB",
        address = "Москва",
        experience = "1-3 года",
        schedule = "Полный день",
        employment = "Полная занятость",
        employerName = "Яндекс",
        employerLogo = "https://logo.png",
        area = "Москва",
        industry = "Информационные технологии, системная интеграция, интернет",
        skills = "Kotlin|Android",
    )
}
