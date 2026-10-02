package ru.yandex.buggyweatherapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import java.util.concurrent.CopyOnWriteArrayList

class LocationTracker private constructor(
    context: Context
) {
    // использую applicationContext
    private val appContext = context.applicationContext
    private val locationManager = appContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private val listeners = CopyOnWriteArrayList<(ru.yandex.buggyweatherapp.model.Location) -> Unit>()

    private val locationListener = LocationListener { location ->
        val newLocation = ru.yandex.buggyweatherapp.model.Location(
            latitude = location.latitude,
            longitude = location.longitude
        )
        notifyListeners(newLocation)
    }

    companion object {
        @Volatile
        private var instance: LocationTracker? = null

        fun getInstance(context: Context): LocationTracker {
            return instance ?: synchronized(this) {
                // беру applicationContext при создании синглтона
                instance ?: LocationTracker(context.applicationContext).also { instance = it }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun startTracking() {
        try {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                5000,
                10f,
                locationListener
            )
        } catch (e: SecurityException) {
            Log.e("LocationTracker", "Permission denied", e)
        } catch (e: Exception) {
            Log.e("LocationTracker", "Error starting location tracking", e)
        }
    }

    private fun notifyListeners(location: ru.yandex.buggyweatherapp.model.Location) {
        for (listener in listeners) {
            listener(location)
        }
    }
}