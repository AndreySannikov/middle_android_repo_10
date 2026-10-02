package ru.yandex.buggyweatherapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(WeatherApiService.BASE_URL)
            // убрал лишний вызов Gson()
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // изменил на получение через lazy для потокобезопасности и отложенного создания
    val weatherApi: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}