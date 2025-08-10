package com.template.apptemplate.data.di

import com.example.sdklib.DriverSdk
import com.example.sdklib.PaymentSdk
import com.example.sdklib.PromoSdk
import com.example.sdklib.TripSdk
import com.template.apptemplate.data.repository.BookingRepositoryImpl
import com.template.apptemplate.domain.repository.BookingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BookingModule {

    @Provides
    @Singleton
    fun provideBookingRepository(
        driverSdk: DriverSdk,
        promoSdk: PromoSdk,
        paymentSdk: PaymentSdk,
        tripSdk: TripSdk,
    ): BookingRepository {
        return BookingRepositoryImpl(driverSdk, promoSdk, paymentSdk, tripSdk)
    }
}
