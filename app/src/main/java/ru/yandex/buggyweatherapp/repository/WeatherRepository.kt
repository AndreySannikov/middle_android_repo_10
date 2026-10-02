package ru.yandex.buggyweatherapp.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.yandex.buggyweatherapp.api.RetrofitInstance
import ru.yandex.buggyweatherapp.model.Location
import ru.yandex.buggyweatherapp.model.WeatherData
import ru.yandex.buggyweatherapp.model.WeatherResponse

class WeatherRepository {

    private val weatherApi = RetrofitInstance.weatherApi

    // пределал для работы с WeatherResponse и корутинами
    suspend fun getWeatherData(location: Location): Result<WeatherData> = withContext(Dispatchers.IO) {
        try {
            val response = weatherApi.getCurrentWeather(location.latitude, location.longitude)
            val weatherData = mapToDomain(response)
            Result.success(weatherData)
        } catch (e: Exception) {
            Log.e("WeatherRepository", "Error fetching weather by coords", e)
            Result.failure(e)
        }
    }

    // также пределал для работы с корутинами
    suspend fun getWeatherByCity(cityName: String): Result<WeatherData> = withContext(Dispatchers.IO) {
        try {
            val response = weatherApi.getWeatherByCity(cityName)
            val weatherData = mapToDomain(response)
            Result.success(weatherData)
        } catch (e: Exception) {
            Log.e("WeatherRepository", "Error fetching weather by city", e)
            Result.failure(e)
        }
    }

    // теперь это обычный маппер из WeatherResponse в WeatherData
    private fun mapToDomain(response: WeatherResponse): WeatherData {
        val weatherInfo = response.weather.firstOrNull()

        return WeatherData(
            cityName = response.cityName,
            country = response.sys.country,
            temperature = response.main.temp,
            feelsLike = response.main.feelsLike,
            minTemp = response.main.tempMin,
            maxTemp = response.main.tempMax,
            humidity = response.main.humidity,
            pressure = response.main.pressure,
            windSpeed = response.wind.speed,
            windDirection = response.wind.deg ?: 0,
            description = weatherInfo?.description ?: "",
            icon = weatherInfo?.icon ?: "",
            cloudiness = response.clouds.all,
            sunriseTime = response.sys.sunrise,
            sunsetTime = response.sys.sunset,
            timezone = response.timezone,
            timestamp = response.timestamp,
            rawApiData = response.toString(),
            rain = response.rain?.oneHour,
            snow = response.snow?.oneHour
        )
    }
}