package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class AreaDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("parentId") val parentId: Int?,
    @SerializedName("name") val name: String,
    @SerializedName("areas") val areas: List<AreaDTO>
)
