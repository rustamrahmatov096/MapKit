package com.example.mapkit.app.util

fun<T> isEqual(first: List<T>?, second: List<T>?): Boolean {

    when{
        first == null && second != null -> return false
        second == null && first != null -> return false
        first?.size != second?.size -> return false
    }

 
    first?.forEachIndexed { index, value ->
        if (second?.get(index) != value) { return false} }

    return true
}