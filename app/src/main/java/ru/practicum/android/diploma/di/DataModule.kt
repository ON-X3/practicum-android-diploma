package ru.practicum.android.diploma.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.data.network.SearchApi

val dataModule = module {
    single<NetworkClient> {
        RetrofitNetworkClient(searchApi = get(), androidContext())
    }
    single<SearchApi> {
        Retrofit.Builder()
            .baseUrl("https://android-diploma.education-services.ru/")
            .addConverterFactory(GsonConverterFactory.create())

            .build()
            .create(SearchApi::class.java)
    }
}
