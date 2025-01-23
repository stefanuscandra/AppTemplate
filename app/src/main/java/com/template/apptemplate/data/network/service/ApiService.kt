package com.template.apptemplate.data.network.service

import retrofit2.http.GET

interface ApiService {

    @GET("/")
    suspend fun fetch()
}
