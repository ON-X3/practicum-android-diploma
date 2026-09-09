package ru.practicum.android.diploma.data.converters

import ru.practicum.android.diploma.data.db.entity.AreaEntity
import ru.practicum.android.diploma.data.dto.AreaDTO

class AreaConverter {
    fun toEntity(area: AreaDTO): AreaEntity {
        return AreaEntity(
            id = area.id,
            parentId = area.parentId,
            name = area.name
        )
    }
}
