package ru.practicum.android.diploma.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    single<CoroutineScope>(named("applicationScope")) {
        CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }
}
