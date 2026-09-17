package ru.practicum.android.diploma.data.storage

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.FilterParameters

interface StorageClient {
    suspend fun saveFilterParameters(filter: FilterParameters)
    suspend fun clearFilterParameters()
    fun getFilterParameters(): Flow<FilterParameters>
}
