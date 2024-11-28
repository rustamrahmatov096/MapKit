package com.example.mapkit.core.ktx

import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.widget.AppCompatTextView
import java.util.regex.Pattern



class RegexMaskTextWatcher(
    regexForInputToMatch: String,
    private val currentView: AppCompatTextView,
    private val placeholder: String
) :
    TextWatcher {

    private val regex = Pattern.compile(regexForInputToMatch)
    private var previousText: String = ""

    override fun afterTextChanged(s: Editable) {
        if (regex.matcher(s).matches() || s.isEmpty()) {
            currentView.text = ""
            currentView.gone()
//            previousText = s.toString()
        } else {
//            s.replace(0, s.length, previousText)
            currentView.visible()
            currentView.text = "Must like $placeholder"
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

}