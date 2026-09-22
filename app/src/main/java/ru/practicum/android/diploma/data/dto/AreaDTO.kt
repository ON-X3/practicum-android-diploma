package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.domain.models.FilterArea

data class AreaDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("parentId") val parentId: Int?,
    @SerializedName("name") val name: String,
    @SerializedName("areas") val areas: List<AreaDTO>
)

fun AreaDTO.toDomainFilterArea(): FilterArea {
    return FilterArea(
        id = id,
        parentId = parentId,
        name = name
    )
}
