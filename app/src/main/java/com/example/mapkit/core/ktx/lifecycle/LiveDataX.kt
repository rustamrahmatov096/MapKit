package com.example.mapkit.core.ktx.lifecycle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.SavedStateHandle
import kotlin.properties.ObservableProperty
import kotlin.reflect.KProperty


inline fun <reified T> LiveData<T>.getLastValue(
    crossinline body: (T) -> Unit
) {
    value?.let { nonNullValue ->
        body(nonNullValue)
        return
    }
    MediatorLiveData<T>().apply {
        this.addSource(this@getLastValue) { newValue ->
            newValue?.let { nonNullValue ->
                this.removeSource(this@getLastValue)
                body(nonNullValue)
            }
        }
    }
}

fun <T> MediatorLiveData<*>.addSourceDisposable(liveData: LiveData<T>, onChange: (T) -> Unit) {
    addSource(liveData) {
        onChange(it)
        removeSource(liveData)
    }
}

fun <T> MediatorLiveData<*>.addSourceDisposableNoAction(liveData: LiveData<T>) {
    addSource(liveData) {
        removeSource(liveData)
    }
}

fun <T> SavedStateHandle.getAsProperty(
    key: String,
    initialValue: T? = get(key)
): ObservableProperty<T?> = object : ObservableProperty<T?>(get(key)) {

    private val liveData = getLiveData<T?>(key, initialValue)

    init {
        if (liveData.value === initialValue) {
            liveData.value = initialValue
        }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? = liveData.value

    override fun afterChange(property: KProperty<*>, oldValue: T?, newValue: T?) {
        liveData.value = newValue
    }
}
