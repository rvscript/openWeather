package com.example.openweatherapp.core.usecases

import com.example.openweatherapp.core.data.WeatherRepository
import com.example.openweatherapp.core.models.WeatherResponse
import javax.inject.Inject

interface MainUseCase {
    suspend fun getWeather(city: String): WeatherResponse
}

class MainUseCaseImpl @Inject constructor(
    private val repository: WeatherRepository
): MainUseCase {
    override suspend fun getWeather(city: String): WeatherResponse {
        return repository.getWeather(city)
    }
}