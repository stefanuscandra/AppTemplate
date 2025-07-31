package com.template.apptemplate.ui.screens.dummy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.apptemplate.domain.model.ReadingItem
import com.template.apptemplate.domain.repositories.TemperatureRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: TemperatureRepository) : ViewModel() {

    private val _data = MutableStateFlow<List<ReadingItem>>(emptyList())
    val data = _data.asStateFlow().onStart {
        getTemperatureData()
    }.stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000), initialValue = emptyList())

    private fun getTemperatureData() {
        viewModelScope.launch {
            repository.getTemperatureData().collect { response ->
                _data.update { response.items.first().readings }
            }
        }
    }
}
