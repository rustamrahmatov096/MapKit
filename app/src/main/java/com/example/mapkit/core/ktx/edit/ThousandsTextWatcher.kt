package com.example.mapkit.core.ktx.edit

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import java.util.StringTokenizer


class ThousandsTextWatcher(
    private var editText: EditText
) : TextWatcher {

    init {
        editText.apply {
            setRawInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
            editText.addTextChangedListener(this@ThousandsTextWatcher)
            imeOptions = EditorInfo.IME_ACTION_NEXT
        }
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable) {
        try {
            editText.removeTextChangedListener(this)
            val value: String? = editText.text?.toString()

            if (!value.isNullOrEmpty()) {

                if (value.startsWith(".")) {
                    editText.setText("0.")
                }

//                if (value.startsWith("0") && !value.startsWith("0.")) {
//                    editText.setText("")
//                }

                if (value.contains(".") && value.length > value.indexOf(".") + 3) {
                    editText.setText(value.substring(0, value.indexOf(".") + 3))
                }

                val str: String = editText.text.toString().replace(" ".toRegex(), "")
                if (value.isNotEmpty()) editText.setText(getDecimalFormattedString(str))
                editText.setSelection(editText.text.toString().length)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            editText.addTextChangedListener(this)
        }
    }

    companion object {

        fun getDecimalFormattedString(value: String): String {
            val lst = StringTokenizer(value, ".")
            var str1 = value
            var str2 = ""
            if (lst.countTokens() > 1) {
                str1 = lst.nextToken()
                str2 = lst.nextToken()
            }
            var str3 = ""
            var i = 0
            var j = -1 + str1.length
            if (str1[-1 + str1.length] == '.') {
                j--
                str3 = "."
            }
            var k = j
            while (true) {
                if (k < 0) {
                    if (str2.isNotEmpty()) str3 = "$str3.$str2"
                    return str3
                }
                if (i == 3) {
                    str3 = " $str3"
                    i = 0
                }
                str3 = str1[k] + str3
                i++
                k--
            }
        }
    }
}