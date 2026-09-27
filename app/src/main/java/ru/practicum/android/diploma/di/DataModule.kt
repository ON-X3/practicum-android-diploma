package ru.practicum.android.diploma.di

import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.converters.VacancyCardConverter
import ru.practicum.android.diploma.data.converters.VacancyDetailConverter
import ru.practicum.android.diploma.data.converters.VacancyDomainConverter
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.db.dao.VacancyDetailDao
import ru.practicum.android.diploma.data.network.AuthInterceptor
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.data.network.SearchApi
import ru.practicum.android.diploma.data.repository.FavoritesRepositoryImpl
import ru.practicum.android.diploma.data.repository.FilterRepositoryImpl
import ru.practicum.android.diploma.data.repository.SearchRepositoryImpl
import ru.practicum.android.diploma.data.repository.SharingRepositoryImpl
import ru.practicum.android.diploma.data.storage.SharedPreferencesStorageClient
import ru.practicum.android.diploma.data.storage.StorageClient
import ru.practicum.android.diploma.domain.api.FavoritesRepository
import ru.practicum.android.diploma.domain.api.FilterRepository
import ru.practicum.android.diploma.domain.api.SearchRepository
import ru.practicum.android.diploma.domain.api.SharingRepository
import ru.practicum.android.diploma.domain.models.FilterParameters

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
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    single<VacancyDetailDao> {
        get<AppDatabase>().vacancyDao()
    }
    single<NetworkClient> { RetrofitNetworkClient(get(), androidContext()) }
    single { VacancyDomainConverter() }
    single { VacancyCardConverter() }
    single { VacancyDetailConverter() }
    single<SearchRepository> { SearchRepositoryImpl(get(), get(), get(), get()) }
    single<StorageClient> {
        SharedPreferencesStorageClient(
            get(),
            get(),
            object : TypeToken<FilterParameters>() {}.type
        )
    }
    factory { Gson() }
    single<FilterRepository> {
        FilterRepositoryImpl(
            get(),
            get(),
            true
        )
    }
    single<FavoritesRepository> { FavoritesRepositoryImpl(get(), get(), get()) }
    factory<SharingRepository> { SharingRepositoryImpl(get()) }
}
