package com.example.mapkit.core.ktx.format

import java.text.DecimalFormat
import kotlin.math.pow

internal val dec = DecimalFormat("###,###,##0.00")

fun String?.toPriceAmount(): String {
    return if (this == null) "0.0"
    else if (this.toDouble() < 0 && this.toDouble() > -1) dec.format(this.toDouble() * 10)
    else dec.format(this.toDouble() / 100)
}

fun Long?.toPriceAmount(): String {
    return if (this == null) "0.0"
    else if (this.toDouble() <= 0 && this.toDouble() > -1) dec.format(this.toDouble() * 10)
    else dec.format(this.toDouble() / 100)
}

fun Double?.toPriceAmount(): String {
    return if (this == null) "0.0"
    else if (this < 0 && this > -1) dec.format(this.toDouble() * 10)
    else dec.format(this / 100)
}

fun String?.toPriceAmount(currency: String?): String =
    currency?.let { toPriceAmount().plus(" $it") } ?: toPriceAmount()

fun Double?.toPriceAmount(currency: String?): String =
    currency?.let { toPriceAmount().plus(" $it") } ?: toPriceAmount()

fun Long?.toPriceAmount(currency: String?): String =
    currency?.let { toPriceAmount().plus(" $it") } ?: toPriceAmount()

fun String.formatExpire(): String {
    return if (length < 4) this
    else StringBuilder(this).insert(2, "/").toString()
}

fun Double.calculateMonthlyPayment(
    loanAmount: Double,
    loanTermInMonths: Double,
    annualInterestRate: Double
): Double {
    val monthlyInterestRate = annualInterestRate / 12 / 100
    val numberOfPayments = loanTermInMonths
    val monthlyPayment = (loanAmount * monthlyInterestRate * (1 + monthlyInterestRate).pow(numberOfPayments)) /
            ((1 + monthlyInterestRate).pow(numberOfPayments) - 1)
    return monthlyPayment
}