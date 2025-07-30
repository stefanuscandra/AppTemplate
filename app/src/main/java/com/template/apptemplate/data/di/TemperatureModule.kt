package com.template.apptemplate.data.di

import com.template.apptemplate.data.network.service.ApiService
import com.template.apptemplate.data.repositories.TemperatureRepositoryImpl
import com.template.apptemplate.domain.repositories.TemperatureRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TemperatureModule {

    @Provides
    @Singleton
    fun provideTemperatureRepository(
        apiService: ApiService,
    ): TemperatureRepository {
        return TemperatureRepositoryImpl(apiService)
    }
}
