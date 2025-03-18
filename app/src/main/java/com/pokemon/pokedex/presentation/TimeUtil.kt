package com.pokemon.pokedex.presentation

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TimeUtil {

    private const val DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss"

    fun getCurrentTimeString(format: String = DEFAULT_FORMAT): String {
        val currentTimeMillis = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        return dateFormat.format(Date(currentTimeMillis))
    }

    fun formatTime(timeInMillis: Long, format: String = DEFAULT_FORMAT): String {
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        return dateFormat.format(Date(timeInMillis))
    }

}