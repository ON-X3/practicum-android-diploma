package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class AreaDTO(
    @SerializedName("id") val id: String,
    @SerializedName("parentId") val parentId: String?,
    @SerializedName("name") val name: String,
    @SerializedName("areas") val areas: String?
)
