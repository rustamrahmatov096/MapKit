package com.example.mapkit.core.broadcast

import android.content.BroadcastReceiver
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status


class SmsBroadcastReceiver : BroadcastReceiver() {

    private var listener: ((String) -> Unit)? = null
    fun setListener(l: (String) -> Unit) {
        listener = l
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            val extras: Bundle = intent.extras ?: return
            val status: Status = extras[SmsRetriever.EXTRA_STATUS] as? Status? ?: return

            when (status.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    val smsCode =
                        (extras.get(SmsRetriever.EXTRA_SMS_MESSAGE) as? String)
                            ?.substringAfter(":")
                            ?.substringBefore(".")
                            ?.trim()
                    smsCode?.let {
                        listener?.invoke(it)
                        val clipboard = getSystemService(
                            context,
                            ClipboardManager::class.java
                        ) as ClipboardManager
                        val clip = ClipData.newPlainText("label", it)
                        clipboard.setPrimaryClip(clip)
                    }
                }
                else -> {
                }
            }
        }
    }
}