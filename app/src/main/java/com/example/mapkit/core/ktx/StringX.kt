package com.example.mapkit.core.ktx

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.pow


val camelRegex = "(?<=[a-zA-Z])[A-Z]".toRegex()
val snakeRegex = "_[a-zA-Z]".toRegex()

// String extensions
fun String.camelToSnakeCase(): String {
    return camelRegex.replace(this) {
        "_${it.value}"
    }.lowercase()
}

fun String.snakeToLowerCamelCase(): String {
    return snakeRegex.replace(this) {
        it.value.replace("_", "").uppercase()
    }
}

fun String.snakeToUpperCamelCase(): String {
    return this
        .snakeToLowerCamelCase()
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}

fun String.camelToUpperSnakeCase(): String {
    return this.camelToSnakeCase().uppercase()
}

fun String.substringMaskedPan(): String {
    val start = this.substring(0, 6)
    val end = this.substring(12)
    return start + end
}

fun String?.equalsPanMaskedPan(maskedPan: String?): Boolean {
    if (this.isNullOrBlank())
        return false
    if (maskedPan.isNullOrBlank())
        return false
    val start = this.substring(0, 6) == maskedPan.substring(0, 6)
    val end = this.substring(12) == maskedPan.substring(12)
    return start && end
}

fun String?.toSumString(): String {
    if (this == null) return "0"
    val a = this.reversed()
    var count = 1
    var b = ""
    a.forEachIndexed { index, c ->
        b += c
        if (count % 3 == 0) {
            b += " "
        }
        count++
    }
    return b.reversed().trim()
}

fun Long?.toSumString(): String {
    if (this == null) return "0"
    val a = this.toString().reversed()
    var count = 1
    var b = ""
    a.forEachIndexed { index, c ->
        b += c
        if (count % 3 == 0) {
            b += " "
        }
        count++
    }
    return b.reversed().trim()
}

fun Double?.toSumString(): String {
    if (this == null) return "0"
    val a = this.toLong().toString().reversed()
    var count = 1
    var b = ""
    a.forEachIndexed { index, c ->
        b += c
        if (count % 3 == 0) {
            b += " "
        }
        count++
    }
    return b.reversed().trim()
}




fun Int?.toSumString(): String {
    if (this == null) return "0"
    val a = this.toString().reversed()
    var count = 1
    var b = ""
    a.forEachIndexed { index, c ->
        b += c
        if (count % 3 == 0) {
            b += " "
        }
        count++
    }
    return b.reversed().trim()
}

fun Long?.toFractionalPart(count: Int): String {
    if (this == null) {
        return "00"
    }
    val divSum = 10.0.pow(count.toDouble())
    val textTemp = String.format("%.${count}f", this.div(divSum))
    val f = textTemp.length.minus(count)
    return textTemp.substring(f)
}

fun Double?.toFractionalPart(count: Int): String {
    if (this == null) {
        return "00"
    }
    val divSum = 10.0.pow(count.toDouble())
    val textTemp = String.format("%.${count}f", this.div(divSum))
    val f = textTemp.length.minus(count)
    return textTemp.substring(f)
}

fun Long?.myDiv(other: Long): Long {
    if (this == null) {
        return 0
    }
    return this.div(other)
}


fun Double?.myDiv(other: Long): Double {
    if (this == null) {
        return 0.0
    }
    return this.div(other)
}

fun Long.toMonthYear(): String {
    val f = SimpleDateFormat("MM.yyyy")
    return f.format(Date(this))
}