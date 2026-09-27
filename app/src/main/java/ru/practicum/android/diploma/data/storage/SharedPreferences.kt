package ru.practicum.android.diploma.data.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import ru.practicum.android.diploma.domain.models.FilterParameters
import java.lang.reflect.Type

class SharedPreferences(context: Context, private val gson: Gson, private val typeOfData: Type) :
    StorageClient {

    private val prefs = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    override suspend fun saveFilterParameters(filter: FilterParameters) {
        val json = gson.toJson(filter, typeOfData)
        prefs.edit { putString(FILTER_PARAMETERS_KEY, json) }
    }

    override suspend fun clearFilterParameters() {
        prefs.edit { remove(FILTER_PARAMETERS_KEY) }
    }

    override fun getFilterParameters(): Flow<FilterParameters?> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
            val json = prefs.getString(FILTER_PARAMETERS_KEY, null)
            if (json == null) {
                trySend(null)
            } else {
                trySend(gson.fromJson(json, typeOfData))
            }
        }

        prefs.registerOnSharedPreferenceChangeListener(listener)

        val json = prefs.getString(FILTER_PARAMETERS_KEY, null)
        if (json == null) {
            trySend(null)
        } else {
            trySend(gson.fromJson(json, typeOfData))
        }

        awaitClose {
            prefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

    companion object {
        private const val SHARED_PREFERENCES_NAME = "android_diploma"
        private const val FILTER_PARAMETERS_KEY = "filter_parameters"
    }
}
