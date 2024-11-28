package com.example.mapkit.app.util

import java.text.SimpleDateFormat
import java.util.TimeZone


/**
 * Created by Zayniddin on October 26, 2022.
 */

fun String.toDate(): String {
    return try {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        formatter.timeZone = TimeZone.getDefault()
        val value = formatter.parse(this)
        val dateFormatter = SimpleDateFormat("dd.MM.yyyy HH:mm")
        dateFormatter.timeZone = TimeZone.getDefault()
        dateFormatter.format(value)
    } catch (e: Exception) {
        "00-00-0000 00:00"
    }
}