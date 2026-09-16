package ru.practicum.android.diploma.domain.models

data class FilterParameters(
    val area: Int? = null,
    val industry: Int? = null,
    val salary: Int? = null,
    val onlyWithSalary: Boolean? = false
)
