package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class IndustryDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)
