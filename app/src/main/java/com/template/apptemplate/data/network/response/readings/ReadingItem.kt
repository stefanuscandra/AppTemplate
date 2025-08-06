package com.template.apptemplate.data.network.response.readings

import com.squareup.moshi.Json

data class ReadingItem(
    @Json(name = "station_id") val stationId: String = "",
    val value: Double = 0.0,
)
