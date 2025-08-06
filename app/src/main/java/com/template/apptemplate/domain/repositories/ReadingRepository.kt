package com.template.apptemplate.domain.repositories

import com.template.apptemplate.data.network.response.readings.Readings
import kotlinx.coroutines.flow.Flow

interface ReadingRepository {
    fun getReadingData(): Flow<List<Readings>>
}
