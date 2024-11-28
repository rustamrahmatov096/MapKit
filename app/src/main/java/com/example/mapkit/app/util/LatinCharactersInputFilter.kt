package com.example.mapkit.app.util

import android.text.InputFilter
import android.text.Spanned

class LatinCharactersInputFilter : InputFilter {

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
            if (!isLatinCharacter(c)) {
                return "" // Reject the input
            }
        }
        return null // Accept the input
    }

    private fun isLatinCharacter(c: Char): Boolean {
        // Check if the character is within the Latin character range
        return (c in '\u0000'..'\u007F') || (c in '\u0080'..'\u00FF')
    }
}
