package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName
data class VacancyListDTO(
    val found: Int,
    val pages: Int,
    val page: Int,
    val items: List<VacancyItemDto>)
data class VacancyItemDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("company")
    val company: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("salary")
    val salary: SalaryDto?,
    @SerializedName("logo")
    val logo: String?
)
data class SalaryDto(
    val from: Int?,
    val to: Int?,
    val currency: String?
)
