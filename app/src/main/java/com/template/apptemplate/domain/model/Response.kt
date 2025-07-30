package com.template.apptemplate.domain.model

import com.squareup.moshi.Json

data class ResponseData(
    val items: List<Reading> = listOf(),
)

data class Reading(
    val timestamp: String = "",
    val readings: List<ReadingItem> = listOf(),
)

data class ReadingItem(
    @Json(name = "station_id") val stationId: String = "",
    val value: Double = 0.0,
)
