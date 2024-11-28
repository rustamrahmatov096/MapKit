package com.example.mapkit.app.util

import java.text.SimpleDateFormat
import java.util.TimeZone


/**
 * Created by Rustam on November 24, 2022.
 */

fun String.toTime(): String {
    return try {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        formatter.timeZone = TimeZone.getDefault()
        val value = formatter.parse(this)
        val dateFormatter = SimpleDateFormat("dd-MM-yyyy \nhh:mm ")
        dateFormatter.timeZone = TimeZone.getDefault()
        dateFormatter.format(value)
    } catch (e: Exception) {
        "00-00-0000 00:00"
    }
}