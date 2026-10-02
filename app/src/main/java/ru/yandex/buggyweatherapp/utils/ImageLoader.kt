package ru.yandex.buggyweatherapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import java.util.concurrent.ConcurrentHashMap

object ImageLoader {

    private lateinit var appContext: Context

    private val imageCache = ConcurrentHashMap<String, Bitmap>()
    private val loaderScope = CoroutineScope(Dispatchers.Main)

    fun initialize(context: Context) {
        appContext = context.applicationContext
    }

    suspend fun loadImage(url: String): Bitmap? = withContext(Dispatchers.IO) {
        if (imageCache.containsKey(url)) {
            return@withContext imageCache[url]
        }
        try {
            val connection = URL(url).openConnection()
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            connection.connect()

            val inputStream = connection.getInputStream()
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()

            if (bitmap != null) {
                imageCache[url] = bitmap
            }
            bitmap
        } catch (e: Exception) {
            null
        }
    }

    fun loadInto(url: String, imageView: ImageView) {
        imageView.tag = url

        if (imageCache.containsKey(url)) {
            imageView.setImageBitmap(imageCache[url])
        } else {
            loaderScope.launch {
                val bitmap = loadImage(url)
                if (bitmap != null && imageView.tag == url) {
                    imageView.setImageBitmap(bitmap)
                }
            }
        }
    }
}