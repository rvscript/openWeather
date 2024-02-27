package com.example.openweatherapp.core.data

import android.util.Log
import com.example.openweatherapp.core.models.WeatherResponse
import com.example.openweatherapp.framework.network.OpenWeatherApi
import com.example.openweatherapp.utility.Constants.API_KEY
import javax.inject.Inject

interface WeatherRepository {
    suspend fun getWeather(city: String): WeatherResponse
}

class WeatherRepositoryImpl @Inject constructor(private val apiService: OpenWeatherApi):
    WeatherRepository {
    override suspend fun getWeather(city: String): WeatherResponse {
        var weatherResponse = WeatherResponse()
        try {
            val response = apiService.getWeather(city, API_KEY)
            response.let { res->
                if (res.isSuccessful) {
                    weatherResponse= response.body()!!
                } else {
                    Log.e("ERROR", "getWeather: ERROR responseBody N/A", )
                }
            }
        } catch (e: Exception) {
            Log.e("ERROR", "getWeather: ${e.printStackTrace()}, message: ${e.message}")
        }
        return weatherResponse
    }
}