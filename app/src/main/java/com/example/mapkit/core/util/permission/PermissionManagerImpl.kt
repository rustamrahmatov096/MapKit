package com.example.mapkit.core.util.permission

import android.Manifest
import android.content.Context
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import javax.inject.Inject


class PermissionManagerImpl @Inject constructor() : PermissionManager {

    private fun checkPermission(context: Context, permission: String, listener: PermissionManagerListener) {
        Dexter.withContext(context)
            .withPermission(permission)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    listener.onAllow()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    listener.onDeny()
                }

                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    override fun checkCameraPermission(context: Context, listener: PermissionManagerListener) {
        checkPermission(context, Manifest.permission.CAMERA, listener)
    }

    override fun checkContactsPermission(context: Context, listener: PermissionManagerListener) {
        checkPermission(context, Manifest.permission.READ_CONTACTS, listener)
    }

    override fun checkCallPhonePermission(context: Context, listener: PermissionManagerListener) {
        checkPermission(context, Manifest.permission.CALL_PHONE, listener)
    }

    override fun checkFineLocationPermission(context: Context, listener: PermissionManagerListener) {
        checkPermission(context, Manifest.permission.ACCESS_FINE_LOCATION, listener)
    }

    override fun checkCoarseLocationPermission(context: Context, listener: PermissionManagerListener) {
        checkPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION, listener)
    }

    override fun checkReadExternalStoragePermission(context: Context, listener: PermissionManagerListener) {
        checkPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE, listener)
    }

    override fun checkWriteExternalStoragePermission(context: Context, listener: PermissionManagerListener) {
        checkPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE, listener)
    }
}