package com.example.mapkit.data.unit

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import com.example.mapkit.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class IdProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {

    var hash = ""

    @SuppressLint("HardwareIds")
    fun getAppId(): String =
        Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

    fun getDeviceNameAndOSVersion() = Build.MODEL + " | Android " + Build.VERSION.RELEASE

//    fun getVersionCode(): String = "34"
//
//    fun getVersionName(): String = "1.2.9"Ï

    fun getDeviceMode(): String = when (BuildConfig.DEBUG) {
        true -> {
            "dev"
        }
        else -> {
            ""
        }
    }

    fun timestamp(): Long {
        return Date().time
    }

    @SuppressLint("HardwareIds")
    fun getAndroidId(): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun getDeviceName(): String {
        return Build.MODEL
    }

    fun deviceType(): String {
        return "android"
    }

    @SuppressLint("HardwareIds")
    fun getDeviceID(): String {
//        return "i-ccf04012-d8fc-4b 1b-8f2b-0be8f10b8fb1"
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun getVersionName(): String {
        return getPackageInfo()?.versionName ?: ""
    }

    fun getVersionCode(): Int {
        return getPackageInfo()?.versionCode ?: 0
    }

    private fun getPackageInfo(): PackageInfo? {
        try {
            return context.packageManager.getPackageInfo(context.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    fun hasDeviceFingerprintSupport(): Boolean {
        val fingerprintManager =
            context.getSystemService(Context.FINGERPRINT_SERVICE) as? FingerprintManager
                ?: return false
        return if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.USE_FINGERPRINT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            false
        } else fingerprintManager.isHardwareDetected && fingerprintManager.hasEnrolledFingerprints()
    }

}