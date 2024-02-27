package com.example.openweatherapp.core.models

data class WeatherResponse(
    val base: String = "",
    val clouds: Clouds = Clouds(0),
    val cod: Int = 0,
    val coord: Coord = Coord(0.0, 0.0),
    val dt: Int = 0,
    val id: Int = 0,
    val main: Main = Main(0.0, 0,0,0.0,0.0,0.0),
    val name: String = "",
    val sys: Sys = Sys("", 0, 0,0,0),
    val timezone: Int = 0,
    val visibility: Int = 0,
    val weather: List<Weather> = ArrayList<Weather>(),
    val wind: Wind = Wind(0, 0.0)
)