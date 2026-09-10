package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Query
import ru.practicum.android.diploma.data.dto.VacancySearchResponse

interface SearchApi {
    @GET("/vacancies?")
    suspend fun search(@Query("text") expression: String): VacancySearchResponse
}

