package com.example.mapkit.app.util

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import com.example.mapkit.R

class CustomDatePickerDialog(
    context: Context,
    listener: OnDateSetListener,
    year: Int,
    month: Int,
    dayOfMonth: Int
) : DatePickerDialog(context, R.style.CustomDatePickerDialog, listener, year, month, dayOfMonth) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Access the DatePicker within the DatePickerDialog and customize it if necessary
        val datePicker = findViewById<DatePicker>(
            R.id.layout_loading
        )
        datePicker?.apply {
            // Customize your DatePicker here if needed
        }
    }
}
