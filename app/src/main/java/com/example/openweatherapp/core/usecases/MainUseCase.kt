package com.example.openweatherapp.core.usecases

import android.content.Context
import com.example.openweatherapp.core.data.WeatherRepository
import com.example.openweatherapp.core.models.WeatherResponse
import javax.inject.Inject

interface MainUseCase {
    suspend fun getWeather(city: String): WeatherResponse
    suspend fun getListOfCities(context: Context): MutableList<String>
}

class MainUseCaseImpl @Inject constructor(
    private val repository: WeatherRepository
): MainUseCase {
    override suspend fun getWeather(city: String): WeatherResponse {
        return repository.getWeather(city)
    }

    override suspend fun getListOfCities(context: Context): MutableList<String> {
        return repository.getCityList(context)
    }

}