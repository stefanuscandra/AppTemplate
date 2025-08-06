package com.template.apptemplate.data.network.service

import com.template.apptemplate.data.network.response.ReadingResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("environment/air-temperature")
    suspend fun getTemperatureData(): Response<ReadingResponse>
}
