package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.SearchRepository
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.domain.util.Resource

class VacancyInteractorImpl(
    private val repository: SearchRepository
) : VacancyInteractor {
    override suspend fun getVacancyDetail(id: String): Resource<VacancyDetail> {
        return repository.getVacancyDetail(id)
    }
}
