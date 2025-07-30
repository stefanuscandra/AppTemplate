package com.template.apptemplate.data.repositories

import com.template.apptemplate.data.network.service.ApiService
import com.template.apptemplate.domain.model.ResponseData
import com.template.apptemplate.domain.repositories.TemperatureRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException

class TemperatureRepositoryImpl(private val apiService: ApiService) : TemperatureRepository {

    override fun getTemperatureData(): Flow<ResponseData> = flow {
        val response = apiService.fetch()
        if (response.isSuccessful) {
            val body = response.body()
            body?.let { emit(it) }
        } else {
            throw HttpException(response)
        }
    }.catch {
        throw it
    }.flowOn(Dispatchers.IO)
}
