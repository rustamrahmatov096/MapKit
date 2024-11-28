package com.example.mapkit.core.ktx


object Validator {
    fun checkAccountDigit(mfo: String, account: String): Boolean {
        var keySum = 0L
        return try {
            val controlString = mfo + account.substring(0, 8) + account.substring(9, 20) + "9"
            for (i in 0..23) {
                keySum += Integer.valueOf(
                    controlString.substring(
                        i,
                        i + 1
                    )
                ) * Integer.valueOf(controlString.substring(i + 1, i + 2)).toLong()
            }
            keySum %= 11
            val keyValue = keySum.toInt()
            var keyString = ""
            keyString = when (keyValue) {
                0 -> "9"
                1 -> "0"
                else -> (11 - keyValue).toString()
            }
            account.substring(8, 9) == keyString
        } catch (e: Exception) {
            false
        }
    }
}