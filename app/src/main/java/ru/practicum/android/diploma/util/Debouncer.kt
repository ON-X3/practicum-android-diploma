package ru.practicum.android.diploma.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class Debouncer<T>(
    private val delayMillis: Long,
    private val coroutineScope: CoroutineScope,
    private val action: suspend (T) -> Unit
) {
    private var debounceJob: Job? = null

    fun invoke(param: T) {
        debounceJob?.cancel()
        debounceJob = coroutineScope.launch {
            delay(delayMillis.milliseconds)
            action(param)
        }
    }

    fun cancel() {
        debounceJob?.cancel()
        debounceJob = null
    }
}
