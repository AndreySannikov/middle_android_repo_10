package ru.yandex.buggyweatherapp.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("name") val cityName: String,
    @SerializedName("sys") val sys: SysDto,
    @SerializedName("main") val main: MainDto,
    @SerializedName("wind") val wind: WindDto,
    @SerializedName("weather") val weather: List<WeatherDto>,
    @SerializedName("clouds") val clouds: CloudsDto,
    @SerializedName("timezone") val timezone: Int,
    @SerializedName("dt") val timestamp: Long,
    @SerializedName("rain") val rain: RainDto?,
    @SerializedName("snow") val snow: SnowDto?
)

data class SysDto(
    @SerializedName("country") val country: String,
    @SerializedName("sunrise") val sunrise: Long,
    @SerializedName("sunset") val sunset: Long
)

data class MainDto(
    @SerializedName("temp") val temp: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("temp_min") val tempMin: Double,
    @SerializedName("temp_max") val tempMax: Double,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("pressure") val pressure: Int
)

data class WindDto(
    @SerializedName("speed") val speed: Double,
    @SerializedName("deg") val deg: Int? = 0
)

data class WeatherDto(
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String
)

data class CloudsDto(
    @SerializedName("all") val all: Int
)

data class RainDto(
    @SerializedName("1h") val oneHour: Double?,
    @SerializedName("3h") val threeHours: Double?
)

data class SnowDto(
    @SerializedName("1h") val oneHour: Double?,
    @SerializedName("3h") val threeHours: Double?
)