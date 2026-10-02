package ru.yandex.buggyweatherapp.api

import retrofit2.http.GET
import retrofit2.http.Query
import ru.yandex.buggyweatherapp.model.WeatherResponse

interface WeatherApiService {

    companion object {
        const val API_KEY = "8fd9a0f2216e2bc16a09102e2af8ab1d"
        const val BASE_URL = "http://api.openweathermap.org/data/2.5/"
    }

    //сделал suspend и добавил DTO объекты, чтобы уйти от JSON
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String = API_KEY,
        @Query("units") units: String = "metric"
    ): WeatherResponse

    @GET("weather")
    suspend fun getWeatherByCity(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String = API_KEY,
        @Query("units") units: String = "metric"
    ): WeatherResponse
}