package com.example.openweatherapp.framework.di

import com.example.openweatherapp.core.data.WeatherRepository
import com.example.openweatherapp.core.data.WeatherRepositoryImpl
import com.example.openweatherapp.core.usecases.MainUseCase
import com.example.openweatherapp.core.usecases.MainUseCaseImpl
import com.example.openweatherapp.framework.network.OpenWeatherApi
import com.example.openweatherapp.utility.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideBaseUrl() = Constants.BASE_URL

    @Provides
    @Singleton
    fun provideRetrofitInstance(BASE_URL: String): OpenWeatherApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(OpenWeatherApi::class.java)

    @Provides
    @Singleton
    fun provideWeatherRepository(apiService: OpenWeatherApi): WeatherRepository {
        return WeatherRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideUseCase(repo: WeatherRepository): MainUseCase {
        return MainUseCaseImpl(repo)
    }
}