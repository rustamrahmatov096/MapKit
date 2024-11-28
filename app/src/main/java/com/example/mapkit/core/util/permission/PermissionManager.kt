package com.example.mapkit.core.util.permission

import android.content.Context

interface PermissionManager {

    fun checkCameraPermission(context: Context, listener: PermissionManagerListener)

    fun checkContactsPermission(context: Context, listener: PermissionManagerListener)

    fun checkCallPhonePermission(context: Context, listener: PermissionManagerListener)

    fun checkFineLocationPermission(context: Context, listener: PermissionManagerListener)

    fun checkCoarseLocationPermission(context: Context, listener: PermissionManagerListener)

    fun checkReadExternalStoragePermission(context: Context, listener: PermissionManagerListener)

    fun checkWriteExternalStoragePermission(context: Context, listener: PermissionManagerListener)
}