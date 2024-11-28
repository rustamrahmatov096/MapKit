package com.example.mapkit.core.ktx.time

import java.util.Calendar
import java.util.Date


inline val Calendar.date: Date
    get() = Date(this.timeInMillis)

inline val Date.calendar: Calendar
    get() = Calendar.getInstance().apply { time = this@calendar }

