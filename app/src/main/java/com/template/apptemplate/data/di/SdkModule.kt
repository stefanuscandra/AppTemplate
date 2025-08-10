package com.template.apptemplate.data.di

import com.example.sdklib.DriverSdk
import com.example.sdklib.PaymentSdk
import com.example.sdklib.PromoSdk
import com.example.sdklib.TripSdk
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SdkModule {

    @Provides
    @Singleton
    fun provideDriverSdk(): DriverSdk = DriverSdk

    @Provides
    @Singleton
    fun providePromoSdk(): PromoSdk = PromoSdk

    @Provides
    @Singleton
    fun provideTripSdk(): TripSdk = TripSdk

    @Provides
    @Singleton
    fun providePaymentSdk(): PaymentSdk = PaymentSdk
}