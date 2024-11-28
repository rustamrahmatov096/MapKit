package com.example.mapkit.app.util

import android.text.InputFilter
import android.text.Spanned

class UppercaseLatinInputFilter : InputFilter {

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        for (i in start until end) {
            val c = source[i]
            if (!isUppercaseLatinCharacter(c)) {
                return "" // Reject the input
            }
        }
        return null // Accept the input
    }

    private fun isUppercaseLatinCharacter(c: Char): Boolean {
        // Check if the character is an uppercase Latin character
        return c in 'A'..'Z' || c in '0'..'9' || c == ' '
    }
}
