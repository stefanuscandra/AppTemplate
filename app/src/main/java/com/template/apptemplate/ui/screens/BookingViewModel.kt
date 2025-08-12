package com.template.apptemplate.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sdklib.DriverInfoResponse
import com.example.sdklib.PaymentResponse
import com.example.sdklib.PromoResponse
import com.example.sdklib.TripDetailsResponse
import com.template.apptemplate.domain.repository.BookingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val repository: BookingRepository,
) : ViewModel() {

    private val _driverInfo = MutableStateFlow<DriverInfoResponse?>(null)
    val driverInfo = _driverInfo.asStateFlow()

    private val _paymentInfo = MutableStateFlow<PaymentResponse?>(null)
    val paymentInfo = _paymentInfo.asStateFlow()

    private val _promoInfo = MutableStateFlow<PromoResponse?>(null)
    val promoInfo = _promoInfo.asStateFlow()

    private val _tripInfo = MutableStateFlow<TripDetailsResponse?>(null)
    val tripInfo = _tripInfo.asStateFlow()


    init {
        getDriverInfo()
        getPaymentInfo()
        getPromoInfo()
        getTripInfo()
    }

    internal fun getDriverInfo() {
        viewModelScope.launch {
            repository.getDriverInfo().catch {
                _driverInfo.update { null }
            }.collect { result ->
                _driverInfo.update { result }
            }
        }
    }

    private fun getPaymentInfo() {
        viewModelScope.launch {
            repository.getPayment().catch {
                _paymentInfo.update { null }
            }.collect { result ->
                _paymentInfo.update { result }
            }
        }
    }

    private fun getPromoInfo() {
        viewModelScope.launch {
            repository.getPromo().catch {
                _promoInfo.update { null }
            }.collect { result ->
                _promoInfo.update { result }
            }
        }
    }

    private fun getTripInfo() {
        viewModelScope.launch {
            repository.getTrip().catch {
                _tripInfo.update { null }
            }.collect { result ->
                _tripInfo.update { result }
            }
        }
    }
}
