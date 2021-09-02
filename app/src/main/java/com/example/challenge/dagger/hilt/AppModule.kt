package com.example.challenge.dagger.hilt

import android.content.Context
import androidx.room.Room
import com.example.challenge.BuildConfig
import com.example.challenge.api.ApiCore
import com.example.challenge.api.common.scheduler.AppSchedulerProvider
import com.example.challenge.api.common.scheduler.SchedulerProvider
import com.example.challenge.api.itunes.service.ItunesService
import com.example.challenge.repository.Repository
import com.example.challenge.repository.RepositoryImpl
import com.example.challenge.room.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * For dependency injection, we use dagger hilt
 * **/

@Module
@InstallIn(ApplicationComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideRetrofit() = ApiCore.getRetrofit(BuildConfig.API_URL)

    @Singleton
    @Provides
    fun provideScheduler():SchedulerProvider = AppSchedulerProvider()

    @Singleton
    @Provides
    fun provideCrimeDataService(retrofit : Retrofit):ItunesService
            = retrofit.create(ItunesService::class.java)

    @Singleton
    @Provides
    fun provideRepository(itunesService: ItunesService,db: AppDatabase,schedulerProvider: SchedulerProvider):Repository
            = RepositoryImpl(itunesService,db,schedulerProvider)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext app:Context): AppDatabase = Room
        .databaseBuilder(app,
            AppDatabase::class.java,
            "itunes_viewer.db")
        .allowMainThreadQueries()
        .build()
}