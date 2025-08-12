package com.template.apptemplate.ui.screens

import com.template.apptemplate.data.network.response.readings.ReadingItem

data class HomeState(
    var readings: List<ReadingItem> = emptyList()
)
