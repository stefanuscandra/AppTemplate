package com.template.apptemplate.data.di

import com.template.apptemplate.data.network.service.ApiService
import com.template.apptemplate.data.repositories.ReadingRepositoryImpl
import com.template.apptemplate.domain.repositories.ReadingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReadingModule {
    @Provides
    @Singleton
    fun provideReadingRepository(apiService: ApiService): ReadingRepository {
        return ReadingRepositoryImpl(service = apiService)
    }
}