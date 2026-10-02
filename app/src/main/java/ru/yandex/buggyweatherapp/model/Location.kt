package ru.yandex.buggyweatherapp.model

data class Location(
    val latitude: Double,
    val longitude: Double,
    val name: String? = null
) {

    // Сделал более красивую и эффективную функцию форматированния в строку
    override fun toString(): String {
        return buildString {
            append("Latitude: $latitude, Longitude: $longitude")
            name?.let { append(", Name: $it") }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Location) return false
        return latitude == other.latitude && longitude == other.longitude
    }
}