package com.example.mapkit.app.util

import android.content.Context
import android.os.Build
import android.util.Log
import java.io.File

object EmulatorCheckUtil {

    fun isEmulator(): Boolean {
        val fingerprint = Build.FINGERPRINT
        val model = Build.MODEL
        val manufacturer = Build.MANUFACTURER
        val brand = Build.BRAND
        val device = Build.DEVICE
        val product = Build.PRODUCT
        val hardware = Build.HARDWARE

        val isEmulator = (fingerprint.startsWith("generic")
                || fingerprint.startsWith("unknown")
                || model.contains("google_sdk")
                || model.contains("Emulator")
                || model.contains("Android SDK built for x86")
                || manufacturer.contains("Genymotion")
                || brand.startsWith("generic") && device.startsWith("generic")
                || product == "google_sdk"
                || hardware.contains("ranchu")) // Ranchu is often used by Android emulators

//        Log.d("MainActivity", "Is Emulator: $isEmulator")

        return isEmulator
    }

    // VirtualBox detection
    fun isRunningOnVirtualBox(): Boolean {
        val hardware = Build.HARDWARE
        val fingerprint = Build.FINGERPRINT
        return (hardware.contains("vbox86") || fingerprint.contains("vbox"))
    }

    fun detectFridaOrMagisk(context: Context): Boolean {
        // Files associated with Frida and Magisk
        val suspiciousFiles = arrayOf(
            // Frida related files
            "/data/local/tmp/re.frida.server",
            "/data/local/tmp/frida-server",
            "/data/local/tmp/frida-server-12.8.0-android-arm64",

            // Magisk related files
            "/sbin/magisk",
            "/sbin/.magisk",
            "/data/adb/magisk",
            "/cache/magisk",
            "/system/bin/magisk",
            "/system/xbin/magisk"
        )

        // Package names associated with Frida and Magisk
        val suspiciousPackages = arrayOf(
            "com.topjohnwu.magisk", // Magisk
            "re.frida.server"       // Frida
        )

        // Check for suspicious files
        suspiciousFiles.forEach {
            if (File(it).exists()) return true
            Log.d("EMULATOR_DETECT", "detectFridaOrMagisk: DETECTED1")
        }

        // Check for suspicious packages
        val packageManager = context.packageManager
        val installedPackages = packageManager.getInstalledPackages(0)
        installedPackages.forEach {
            if (suspiciousPackages.contains(it.packageName)) return true
            Log.d("EMULATOR_DETECT", "detectFridaOrMagisk: DETECTED2")
        }

        return false
    }

}