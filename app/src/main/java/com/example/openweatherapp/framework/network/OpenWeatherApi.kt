package com.example.openweatherapp.framework.network

import com.example.openweatherapp.core.models.WeatherResponse
import com.example.openweatherapp.utility.Constants.API_PATH
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {
    @GET(API_PATH)
    suspend fun getWeather(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String
    ): Response<WeatherResponse>
}