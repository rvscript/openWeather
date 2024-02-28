package com.example.openweatherapp.core.data

import android.content.Context
import android.util.Log
import com.example.openweatherapp.core.models.WeatherResponse
import com.example.openweatherapp.framework.network.OpenWeatherApi
import com.example.openweatherapp.utility.Constants.API_KEY
import org.json.JSONArray
import javax.inject.Inject

interface WeatherRepository {
    suspend fun getWeather(city: String): WeatherResponse
    suspend fun getCityList(context: Context): MutableList<String>
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

    override suspend fun getCityList(context: Context): MutableList<String> {
        val jsonString = getJsonFromAssets(context, "current.city.list.json")
        return parseJsonToList(jsonString)
    }

    private fun getJsonFromAssets(context: Context, fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use {
            it.readText()
        }
    }


    // create a separate repository to handle installed files
    private fun parseJsonToList(jsonString: String): MutableList<String> {
        val jsonArray = JSONArray(jsonString)
        val cityList: MutableList<String> = mutableListOf()
        try {
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                jsonObject.getString("name").let {
                    val city = jsonObject.getString("name")
                    val country = jsonObject.getString("country")
                    if (country == "US") {
                        cityList.add("$city, $country")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("ERROR", "parseJsonToList: ${e.message}, ${e.stackTrace}" )
        }
        cityList.sort()
        return cityList
    }
}