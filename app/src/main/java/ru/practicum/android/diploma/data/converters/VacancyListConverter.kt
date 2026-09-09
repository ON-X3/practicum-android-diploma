package ru.practicum.android.diploma.data.converters

import ru.practicum.android.diploma.data.db.entity.VacancyListEntity
import ru.practicum.android.diploma.data.dto.VacancyListDTO

class VacancyListConverter {
    fun toEntity(vacancyList: VacancyListDTO): List<VacancyListEntity>{
        return vacancyList.items.map { item ->
            VacancyListEntity(
                id = item.id,
                name = item.name,
                company = item.company,
                city = item.city,
                salaryFrom = item.salary?.from,
                salaryTo = item.salary?.to,
                currency = item.salary?.currency,
                logo = item.logo
            )
        }
    }
}
