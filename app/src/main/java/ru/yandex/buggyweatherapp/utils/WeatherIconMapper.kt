package ru.yandex.buggyweatherapp.utils

import androidx.compose.ui.graphics.Color
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object WeatherIconMapper {

    // вынес форматтер для экономии памяти
    private val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())

    // добавил учитывание часового пояса конкретного города
    fun formatTimestamp(timestamp: Long, timezoneOffset: Int = 0): String {
        val date = Date((timestamp + timezoneOffset) * 1000)
        timeFormatter.timeZone = TimeZone.getTimeZone("UTC")
        return timeFormatter.format(date)
    }

    // убрал лишнюю конкатенацию строк
    fun getWeatherDescription(description: String, temperature: Double): String {
        val capitalizedDescription = description.replaceFirstChar { it.uppercase() }
        return "$capitalizedDescription, ${temperature.toInt()}°C"
    }

    // вместо заглушек возвращаю иконоки (системные)
    fun getWeatherIconResource(iconCode: String): Int {
        return when (iconCode) {
            "01d" -> android.R.drawable.ic_menu_compass
            "01n" -> android.R.drawable.ic_menu_mylocation
            "02d", "02n" -> android.R.drawable.ic_menu_report_image
            "03d", "03n", "04d", "04n" -> android.R.drawable.ic_menu_gallery
            "09d", "09n", "10d", "10n" -> android.R.drawable.ic_menu_slideshow
            "11d", "11n" -> android.R.drawable.ic_delete
            "13d", "13n" -> android.R.drawable.ic_input_add
            else -> android.R.drawable.ic_menu_help
        }
    }

    // вместо заглушек возвращаю цвета
    fun getBackgroundColor(weatherId: Int, temperature: Double): Color {
        return when (weatherId) {
            in 200..299 -> Color(0xFF4A5568)
            in 300..399 -> Color(0xFF718096)
            in 500..599 -> Color(0xFF4A5568)
            in 600..699 -> Color(0xFFE2E8F0)
            in 700..799 -> Color(0xFFA0AEC0)
            800 -> {
                when {
                    temperature > 30 -> Color(0xFFED8936)
                    temperature > 15 -> Color(0xFFECC94B)
                    else -> Color(0xFF63B3ED)
                }
            }
            in 801..804 -> Color(0xFFCBD5E0)
            else -> Color(0xFFEDF2F7)
        }
    }
}