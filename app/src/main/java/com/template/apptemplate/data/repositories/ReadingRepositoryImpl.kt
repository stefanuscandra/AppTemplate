package com.template.apptemplate.data.repositories

import com.template.apptemplate.data.network.response.readings.Readings
import com.template.apptemplate.data.network.service.ApiService
import com.template.apptemplate.domain.repositories.ReadingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException

class ReadingRepositoryImpl(private val service: ApiService) : ReadingRepository {

    override fun getReadingData(): Flow<List<Readings>> = flow {
        val response = service.getTemperatureData()
        if (response.isSuccessful) {
            response.body()?.items?.let { data ->
                emit(data)
            }
        } else {
            throw HttpException(response)
        }
    }.flowOn(Dispatchers.IO)
}
