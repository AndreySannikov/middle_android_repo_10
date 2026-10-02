package ru.yandex.buggyweatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ru.yandex.buggyweatherapp.model.Location
import ru.yandex.buggyweatherapp.model.WeatherData
import ru.yandex.buggyweatherapp.repository.LocationRepository
import ru.yandex.buggyweatherapp.repository.WeatherRepository
import ru.yandex.buggyweatherapp.utils.ImageLoader
import kotlin.time.Duration.Companion.minutes

// сделал единый стейт экрана
data class WeatherUiState(
    val weatherData: WeatherData? = null,
    val currentLocation: Location? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val cityName: String = ""
)

// лучше внедрять через dagger
class WeatherViewModel(
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository = WeatherRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    init {
        fetchCurrentLocationWeather()
        startAutoRefresh()
    }

    fun fetchCurrentLocationWeather() {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val location = locationRepository.getCurrentLocation()
            if (location != null) {
                val cityNameFromLocation = locationRepository.getCityNameFromLocation(location) ?: ""
                _uiState.update {
                    it.copy(currentLocation = location, cityName = cityNameFromLocation)
                }
                getWeatherForLocation(location)
            } else {
                _uiState.update { it.copy(isLoading = false, error = "Unable to get current location") }
            }
        }
    }

    fun getWeatherForLocation(location: Location) {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val result = weatherRepository.getWeatherData(location)

            result.onSuccess { data ->
                _uiState.update { it.copy(isLoading = false, weatherData = data) }
            }.onFailure { exception ->
                _uiState.update { it.copy(isLoading = false, error = exception.message ?: "Unknown error") }
            }
        }
    }

    fun searchWeatherByCity(city: String) {
        if (city.isBlank()) {
            _uiState.update { it.copy(error = "City name cannot be empty") }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val result = weatherRepository.getWeatherByCity(city)

            result.onSuccess { data ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        weatherData = data,
                        cityName = data.cityName,
                        currentLocation = Location(0.0, 0.0, data.cityName)
                    )
                }
            }.onFailure { exception ->
                _uiState.update { it.copy(isLoading = false, error = exception.message ?: "Unknown error") }
            }
        }
    }

    fun formatTemperature(temp: Double): String {
        return "${temp.toInt()}°C"
    }

    fun loadWeatherIcon(iconCode: String) {
        viewModelScope.launch {
            val iconUrl = "https://openweathermap.org/img/wn/$iconCode@2x.png"
            ImageLoader.loadImage(iconUrl)
        }
    }

    private fun startAutoRefresh() {
        viewModelScope.launch(Dispatchers.Default) {
            while (isActive) {
                delay(1.minutes)
                _uiState.value.currentLocation?.let { location ->
                    getWeatherForLocation(location)
                }
            }
        }
    }

    fun toggleFavorite() {
        _uiState.update { state ->
            state.weatherData?.let { data ->
                state.copy(weatherData = data.copy(isFavorite = !data.isFavorite))
            } ?: state
        }
    }
}