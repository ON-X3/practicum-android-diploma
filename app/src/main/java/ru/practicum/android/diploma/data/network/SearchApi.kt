package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Query
import ru.practicum.android.diploma.data.dto.AreaDTO
import ru.practicum.android.diploma.data.dto.IndustryDTO
import ru.practicum.android.diploma.data.dto.VacancySearchResponse

interface SearchApi {
    @GET("vacancies")
    suspend fun search(
        @Query("text") expression: String,
        @Query("page") page: Int,
        @Query("area") area: Int? = null,
        @Query("industry") industry: Int? = null,
        @Query("salary") salary: Int? = null,
        @Query("only_with_salary") onlyWithSalary: Boolean? = null,
    ): VacancySearchResponse

    @GET("areas")
    suspend fun getFilterAreas(): List<AreaDTO>

    @GET("industries")
    suspend fun getFilterIndustries(): List<IndustryDTO>
}
