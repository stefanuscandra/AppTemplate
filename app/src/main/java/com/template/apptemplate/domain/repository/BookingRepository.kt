package com.template.apptemplate.domain.repository

import com.example.sdklib.DriverInfoResponse
import com.example.sdklib.PaymentResponse
import com.example.sdklib.PromoResponse
import com.example.sdklib.TripDetailsResponse
import kotlinx.coroutines.flow.Flow

interface BookingRepository {
    fun getDriverInfo(): Flow<DriverInfoResponse>
    fun getPromo(): Flow<PromoResponse>
    fun getPayment(): Flow<PaymentResponse>
    fun getTrip(): Flow<TripDetailsResponse>
}
