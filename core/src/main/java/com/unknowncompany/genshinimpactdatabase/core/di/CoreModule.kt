package com.unknowncompany.genshinimpactdatabase.core.di

import androidx.room.Room
import com.unknowncompany.genshinimpactdatabase.core.data.source.local.LocalDataSource
import com.unknowncompany.genshinimpactdatabase.core.data.source.local.room.GenshinImpactDatabase
import com.unknowncompany.genshinimpactdatabase.core.data.source.remote.RemoteDataSource
import com.unknowncompany.genshinimpactdatabase.core.data.source.remote.network.ApiConfig
import com.unknowncompany.genshinimpactdatabase.core.data.source.remote.network.ApiService
import com.unknowncompany.genshinimpactdatabase.core.domain.repository.IGenshinImpactRepository
import com.unknowncompany.genshinimpactdatabase.core.utils.AppExecutors
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val databaseModule = module {
    factory { get<GenshinImpactDatabase>().genshinImpactDao() }
    single {
        Room.databaseBuilder(
            androidContext(),
            GenshinImpactDatabase::class.java,
            "GenshinImpact.db"
        ).fallbackToDestructiveMigration().build()
    }
}

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    factory { AppExecutors() }
    single<IGenshinImpactRepository> {
        com.unknowncompany.genshinimpactdatabase.core.data.GenshinImpactRepository(get(),
            get(),
            get())
    }
}