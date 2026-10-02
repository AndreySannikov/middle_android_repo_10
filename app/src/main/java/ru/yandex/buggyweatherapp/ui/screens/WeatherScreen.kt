package ru.yandex.buggyweatherapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.yandex.buggyweatherapp.ui.components.DetailedWeatherCard
import ru.yandex.buggyweatherapp.ui.components.LocationSearch
import ru.yandex.buggyweatherapp.viewmodel.WeatherViewModel

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
    modifier: Modifier = Modifier
) {
    // подписываюсь на единый StateFlow с учётом жизненного цикла экрана и дальше обрабатыаваю уже его одного
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // теперь без бизнес логики, просто пересылает действия пользователя во ViewModel
        LocationSearch(
            onCitySearch = { city ->
                viewModel.searchWeatherByCity(city)
            },
            onLocationRequest = {
                viewModel.fetchCurrentLocationWeather()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading && uiState.weatherData == null) {
            Text(
                text = "Loading weather data...",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )
        }

        uiState.error?.let { errorText ->
            Text(
                text = errorText,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )
        }

        uiState.weatherData?.let { weather ->
            DetailedWeatherCard(
                weather = weather,
                onFavoriteClick = {
                    viewModel.toggleFavorite()
                },
                onRefreshClick = {
                    viewModel.fetchCurrentLocationWeather()
                }
            )
        }
    }
}