package ru.practicum.android.diploma.domain.models

data class FilterParameters(
    val area: FilterAreaDetails? = null,
    val industry: Industry? = null,
    val salary: Int? = null,
    val onlyWithSalary: Boolean? = false
)

data class FilterAreaDetails(
    val country: CountryArea?,
    val region: RegionArea?
)

data class CountryArea(
    val countryId: Int,
    val countryName: String
)

data class RegionArea(
    val regionId: Int,
    val regionName: String
)

data class Industry(
    val industryId: Int,
    val industryName: String
)
