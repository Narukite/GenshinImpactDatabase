package com.unknowncompany.genshinimpactdatabase

import android.app.Application
import com.unknowncompany.genshinimpactdatabase.core.di.databaseModule
import com.unknowncompany.genshinimpactdatabase.core.di.networkModule
import com.unknowncompany.genshinimpactdatabase.core.di.repositoryModule
import com.unknowncompany.genshinimpactdatabase.di.useCaseModule
import com.unknowncompany.genshinimpactdatabase.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class GenshinImpactApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@GenshinImpactApplication)
            modules(
                listOf(
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }
    }
}