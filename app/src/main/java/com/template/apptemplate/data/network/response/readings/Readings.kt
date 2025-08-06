package com.template.apptemplate.data.network.response.readings

data class Readings(
    val timestamp: String = "",
    val readings: List<ReadingItem> = emptyList(),
)
