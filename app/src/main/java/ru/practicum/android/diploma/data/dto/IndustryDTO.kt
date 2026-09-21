package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.domain.models.Industry

data class IndustryDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)

fun IndustryDTO.toDomainIndustry(): Industry {
    return Industry(
        industryId = id,
        industryName = name
    )
}
