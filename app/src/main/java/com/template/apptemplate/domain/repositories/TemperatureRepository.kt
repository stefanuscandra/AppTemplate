package com.template.apptemplate.domain.repositories

import com.template.apptemplate.domain.model.ResponseData
import kotlinx.coroutines.flow.Flow

interface TemperatureRepository {
    fun getTemperatureData(): Flow<ResponseData>
}
