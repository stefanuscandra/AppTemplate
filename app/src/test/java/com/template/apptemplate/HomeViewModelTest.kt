package com.template.apptemplate

import com.template.apptemplate.data.network.response.readings.Readings
import com.template.apptemplate.domain.repositories.ReadingRepository
import com.template.apptemplate.ui.screens.HomeViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel
    private lateinit var repository: ReadingRepository

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        repository = mockk()
    }

    @Test
    fun `test getTemperatureData is correct`() = runTest {
        // Given
        coEvery { repository.getReadingData() } returns flowOf(listOf(Readings(readings = emptyList())))

        // When
        viewModel = HomeViewModel(repository = repository)
        viewModel.getTemperatureData()

        // Then
        advanceUntilIdle()
        Assert.assertEquals(0, viewModel.state.value.readings.size)
    }
}