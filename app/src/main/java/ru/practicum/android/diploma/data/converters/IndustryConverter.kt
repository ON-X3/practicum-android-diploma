package ru.practicum.android.diploma.data.converters

import ru.practicum.android.diploma.data.db.entity.IndustryEntity
import ru.practicum.android.diploma.data.dto.IndustryDTO

class IndustryConverter {
    fun toEntity(industry: IndustryDTO): IndustryEntity {
        return IndustryEntity(
            id = industry.id,
            name = industry.name
        )
    }
}
