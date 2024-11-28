package com.example.mapkit.core.ktx


import android.os.Build
import android.text.Html
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat


fun AppCompatTextView.setTextAsync(text: String) {
    val params: PrecomputedTextCompat.Params = TextViewCompat.getTextMetricsParams(this)
    this.setTextFuture(PrecomputedTextCompat.getTextFuture(text, params, null))
}


var AppCompatTextView.html: String
    get() = text.toString()
    set(value) {
        val params = TextViewCompat.getTextMetricsParams(this)
        setTextFuture(
            PrecomputedTextCompat.getTextFuture(
                @Suppress("DEPRECATION")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Html.fromHtml(value, Html.FROM_HTML_MODE_COMPACT)
                else Html.fromHtml(value), params, null
            )
        )
    }