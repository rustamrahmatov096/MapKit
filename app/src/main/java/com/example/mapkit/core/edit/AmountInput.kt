package com.example.mapkit.core.edit

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.example.mapkit.core.ktx.edit.ThousandsTextWatcher


class AmountInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    init {
        ThousandsTextWatcher(this)
    }

    val value: String?
        get() = text?.replace(" ".toRegex(), "")

    val amount: Double
        get() = value?.toDoubleOrNull() ?: 0.0

    /** amountLong o'zi 100 ga ko'paytirib qaytaradi ***/

    val amountLong: Long
        get() = fixedText.toLongOrNull() ?: 0L

    private val fixedText: String
        get() {
            if (text.isNullOrEmpty()) {
                return "000"
            } else {
                if (text?.contains(".") == true) {
                    var beforeDot = text.toString().substringBefore(".")// oldin
                    var afterDot = text.toString().substringAfter(".") // keyin
                    beforeDot = clearFormat(beforeDot)
                    afterDot = clearFormat(afterDot)
                    if (beforeDot.isEmpty()) {
                        beforeDot = "0"
                    }
                    afterDot = when (afterDot.length) {
                        0 -> {
                            "0"
                        }
                        1 -> {
                            "${afterDot}0"
                        }
                        2 -> {
                            afterDot
                        }
                        else -> {
                            afterDot.take(2)
                        }
                    }
                    return beforeDot + afterDot
                } else {
                    return clearFormat(text.toString()) + "00"
                }
            }
        }


    private fun clearFormat(text: String): String = text.replace(Regex("[\\D]"), "")

}