package com.example.openweatherapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.openweatherapp.core.data.WeatherRepository
import com.example.openweatherapp.core.models.WeatherResponse
import com.example.openweatherapp.core.usecases.MainUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.EMPTY_RESPONSE
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCase: MainUseCase
): ViewModel() {
    private val _weatherData = MutableLiveData<WeatherResponse>()
    val weatherData: LiveData<WeatherResponse>
        get() = _weatherData
    private fun getWeather() = viewModelScope.launch {
        var weatherResponse = WeatherResponse()
        withContext(IO) {
            weatherResponse = useCase.getWeather("New York")
        }
        withContext(IO) {
            _weatherData.postValue(weatherResponse)
        }
    }
    init {
        getWeather()
    }
}