package com.template.apptemplate.data.repository

import com.example.sdklib.DriverInfoResponse
import com.example.sdklib.DriverSdk
import com.example.sdklib.PaymentResponse
import com.example.sdklib.PaymentSdk
import com.example.sdklib.PromoResponse
import com.example.sdklib.PromoSdk
import com.example.sdklib.TripDetailsResponse
import com.example.sdklib.TripSdk
import com.template.apptemplate.domain.repository.BookingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn

class BookingRepositoryImpl(
    private val driverSdk: DriverSdk,
    private val promoSdk: PromoSdk,
    private val paymentSdk: PaymentSdk,
    private val tripSdk: TripSdk,
) : BookingRepository {
    override fun getDriverInfo(): Flow<DriverInfoResponse> = callbackFlow {
        driverSdk.getInstance().getDriverInfo {
            trySend(it)
        }
        awaitClose {}
    }.flowOn(Dispatchers.IO)

    override fun getPromo(): Flow<PromoResponse> = callbackFlow {
        promoSdk.getInstance().getPromoInfo {
            trySend(it)
        }
        awaitClose {}
    }.flowOn(Dispatchers.IO)

    override fun getPayment(): Flow<PaymentResponse> = callbackFlow {
        paymentSdk.getInstance().getPaymentInfo {
            trySend(it)
        }
        awaitClose {}
    }.flowOn(Dispatchers.IO)

    override fun getTrip(): Flow<TripDetailsResponse> = callbackFlow {
        tripSdk.getInstance().getTripDetails {
            trySend(it)
        }
        awaitClose {}
    }.flowOn(Dispatchers.IO)
}
