package com.template.apptemplate.data.network.response

import com.template.apptemplate.data.network.response.readings.Readings

data class ReadingResponse(
    val items: List<Readings> = emptyList(),
)
