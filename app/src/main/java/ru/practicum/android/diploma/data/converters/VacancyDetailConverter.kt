package ru.practicum.android.diploma.data.converters

import ru.practicum.android.diploma.data.db.entity.VacancyDetailEntity
import ru.practicum.android.diploma.data.dto.VacancyDetailDTO

class VacancyDetailConverter {
    fun toEntity(vacancy: VacancyDetailDTO): VacancyDetailEntity {
        return VacancyDetailEntity(
            id = vacancy.id,
            name = vacancy.name,
            description = vacancy.description,
            url = vacancy.url,
            salary = vacancy.salary,
            address = vacancy.address,
            experience = vacancy.experience,
            schedule = vacancy.schedule,
            employment = vacancy.employment,
            contacts = vacancy.contacts,
            area = vacancy.area,
            employer = vacancy.employer,
            industry = vacancy.industry,
            skills = vacancy.skills
        )
    }
}
