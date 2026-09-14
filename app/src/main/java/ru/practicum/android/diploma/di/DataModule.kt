package ru.practicum.android.diploma.di

import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.converters.VacancyDomainConverter
import ru.practicum.android.diploma.data.network.AuthInterceptor
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.data.network.SearchApi
import ru.practicum.android.diploma.data.repository.SearchRepositoryImpl
import ru.practicum.android.diploma.domain.api.SearchRepository

val dataModule = module {
    single<SearchApi> {
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(BuildConfig.API_ACCESS_TOKEN))
            .build()

        Retrofit.Builder()
            .baseUrl("https://android-diploma.education-services.ru/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SearchApi::class.java)
    }
    single<NetworkClient> { RetrofitNetworkClient(get(), androidContext()) }
    single { VacancyDomainConverter() }
    single<SearchRepository> { SearchRepositoryImpl(get(), get()) }
}