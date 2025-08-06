package com.template.apptemplate.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.apptemplate.domain.repositories.ReadingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: ReadingRepository) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        getTemperatureData()
    }

    fun onAction(action: HomeAction) {

    }

    internal fun getTemperatureData() {
        viewModelScope.launch {
            repository.getReadingData().collect { result ->
                _state.update {
                    it.copy(readings = result.first().readings)
                }
            }
        }
    }
}
