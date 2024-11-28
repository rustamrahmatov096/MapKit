package com.example.mapkit.data.local

import com.example.mapkit.core.model.ThemeType

interface AppCache {
    var accessToken: String?
    var identyToken: String?
    var language: String
    var myIdCode: String?
    var businessId: String?
    var phone: String?
    var serialNumber: String?
    var refreshToken: String?
    var pubKey:String?
    var isBiometricEnabled:Boolean
    var pinCode: String?
    var isPassedPin: Boolean
    var exitTime: String
    var totalBalanceVisible: Boolean

    var chatToken: String

    var FCMToken: String
    var enableNotification: Boolean

    var hash: String?
    var companyName: String?
    var enteredUserName: String?
    var mfo: String?

    var themeType: ThemeType

    fun requireToken(): String
    fun requireAuthToken(): String
    fun clearPreferences(): Boolean
}