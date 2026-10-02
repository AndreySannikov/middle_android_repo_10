package ru.yandex.buggyweatherapp.ui.components

import android.widget.ImageView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import ru.yandex.buggyweatherapp.model.WeatherData
import ru.yandex.buggyweatherapp.utils.ImageLoader
import ru.yandex.buggyweatherapp.utils.WeatherIconMapper

@Composable
fun DetailedWeatherCard(
    weather: WeatherData,
    onFavoriteClick: () -> Unit,
    onRefreshClick: () -> Unit,
) {
    val context = LocalContext.current
    val imageView = remember { ImageView(context) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = weather.cityName,
                    style = MaterialTheme.typography.headlineMedium
                )

                // добавил обработку клика и передачу во ViewModel
                IconButton(onClick = onFavoriteClick) {
                    Icon(
                        imageVector = if (weather.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite"
                    )
                }

                IconButton(onClick = onFavoriteClick) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh"
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                AndroidView(
                    factory = { imageView },
                    modifier = Modifier.size(50.dp)
                ) { view ->
                    val iconUrl = "https://openweathermap.org/img/wn/${weather.icon}@2x.png"
                    ImageLoader.loadInto(iconUrl, view)
                }

                Text(
                    text = "${weather.temperature}°C",
                    style = MaterialTheme.typography.headlineLarge
                )
            }

            Text(
                text = weather.description.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            // заменил ломающий верстку LazyColumn на обычный Column
            Column(modifier = Modifier.fillMaxWidth()) {
                WeatherDataRow("Feels like", "${weather.feelsLike}°C")
                WeatherDataRow("Min/Max", "${weather.minTemp}°C / ${weather.maxTemp}°C")
                WeatherDataRow("Humidity", "${weather.humidity}%")
                WeatherDataRow("Pressure", "${weather.pressure} hPa")
                WeatherDataRow("Wind", "${weather.windSpeed} m/s")
                WeatherDataRow(
                    "Sunrise",
                    WeatherIconMapper.formatTimestamp(weather.sunriseTime, weather.timezone)
                )
                WeatherDataRow(
                    "Sunset",
                    WeatherIconMapper.formatTimestamp(weather.sunsetTime, weather.timezone)
                )
            }
        }
    }

    // удалил DisposableEffect который блокировал основной поток
}

@Composable
private fun WeatherDataRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Text(text = value, style = MaterialTheme.typography.bodyMedium)
    }
}