package com.example.openweatherapp.presentation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.openweatherapp.core.models.WeatherResponse
import com.example.openweatherapp.core.usecases.MainUseCase
import com.example.openweatherapp.utility.Constants.PREF_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCase: MainUseCase
): ViewModel() {
    private val _weatherData = MutableLiveData<WeatherResponse>()
    val weatherData: LiveData<WeatherResponse>
        get() = _weatherData

    val _cityList = MutableLiveData<MutableList<String>>()
    val cityList: LiveData<MutableList<String>>
        get() = _cityList

    fun getWeather(city: String) = viewModelScope.launch {
        var weatherResponse = WeatherResponse()
        withContext(IO) {
            weatherResponse = useCase.getWeather(city)
        }
        withContext(IO) {
            _weatherData.postValue(weatherResponse)
        }
    }
    suspend fun getListOfCities(context: Context) {
        _cityList.postValue(useCase.getListOfCities(context))
    }

    fun addToSharedPreferences(context: Context, key: String, value: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getFromSharedPreferences(context: Context, key: String, defaultValue: String): String {
        val sharedPreferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }
}