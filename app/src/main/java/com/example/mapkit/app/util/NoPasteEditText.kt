package com.example.mapkit.app.util

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class NoPasteEditText(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {
    override fun onTextContextMenuItem(id: Int): Boolean {
        when (id) {
            android.R.id.cut, android.R.id.copy, android.R.id.paste -> return false
        }
        return super.onTextContextMenuItem(id)
    }
}
