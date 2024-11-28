package com.example.mapkit.core.ktx.edit

import android.text.Editable
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


inline val EditText.value: String
    get() = text?.toString() ?: String()

inline val TextView.value: String
    get() = text?.toString() ?: String()


@ExperimentalCoroutinesApi
fun EditText.editFlow(): Flow<Editable?> = callbackFlow {
    val listener = doAfterTextChanged { trySend(it) }
    awaitClose { removeTextChangedListener(listener) }
}