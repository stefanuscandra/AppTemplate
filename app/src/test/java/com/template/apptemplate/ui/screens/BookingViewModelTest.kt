package com.template.apptemplate.ui.screens

import com.example.sdklib.DriverInfoResponse
import com.template.apptemplate.domain.repository.BookingRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BookingViewModelTest {

    private lateinit var repository: BookingRepository
    private lateinit var viewModel: BookingViewModel
    private val dispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        repository = mockk()
        coEvery { repository.getDriverInfo() } returns flowOf()
        coEvery { repository.getPromo() } returns flowOf()
        coEvery { repository.getPayment() } returns flowOf()
        coEvery { repository.getTrip() } returns flowOf()
    }

    @Test
    fun `test success getDriverInfo should return data and update state correctly`() = runTest {
        // Given
        val mockResponse = DriverInfoResponse(
            id = "123",
            name = "John",
            image = ""
        )
        coEvery { repository.getDriverInfo() } returns flowOf(mockResponse)
        viewModel = BookingViewModel(repository)

        // When
        viewModel.getDriverInfo()
        advanceUntilIdle()
        val state = viewModel.driverInfo.value

        // Then
        Assert.assertEquals(mockResponse.id, state?.id.orEmpty())
        Assert.assertEquals(mockResponse.name, state?.name.orEmpty())
    }

    @Test
    fun `test error getDriverInfo should not return data and state still null`() = runTest {
        // Given
        coEvery { repository.getDriverInfo() } returns flow { throw Exception("network error") }
        viewModel = BookingViewModel(repository)

        // When
        viewModel.getDriverInfo()
        advanceUntilIdle()
        val state = viewModel.driverInfo.value

        // Then
        Assert.assertEquals(null, state)
    }
}