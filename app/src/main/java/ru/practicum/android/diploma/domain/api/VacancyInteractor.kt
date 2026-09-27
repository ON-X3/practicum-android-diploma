package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.domain.util.Resource

interface VacancyInteractor {
    suspend fun getVacancyDetail(id: String): Resource<VacancyDetail>
}
