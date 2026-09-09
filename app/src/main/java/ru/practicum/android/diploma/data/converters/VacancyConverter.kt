package ru.practicum.android.diploma.data.converters

import ru.practicum.android.diploma.data.db.entity.VacancyEntity
import ru.practicum.android.diploma.data.dto.VacancyDTO

class VacancyConverter {
    fun toEntity(vacancy: VacancyDTO): VacancyEntity{
        return VacancyEntity(
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
            skills =  vacancy.skills
        )
    }
}
