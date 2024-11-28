package com.example.mapkit.core.broadcast

import android.content.Context
import android.content.IntentFilter
import androidx.lifecycle.LiveData
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.auth.api.phone.SmsRetrieverClient
import com.google.android.gms.tasks.Task
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class SmsAutoLiveData @Inject constructor(
    @ApplicationContext private val context: Context
) : LiveData<String>() {

    private val smsReceiver: SmsBroadcastReceiver by lazy { SmsBroadcastReceiver() }
    private val intentFilter: IntentFilter by lazy {
        IntentFilter().apply {
            addAction(SmsRetriever.SMS_RETRIEVED_ACTION)
        }
    }

    init {
        context.applicationContext.registerReceiver(smsReceiver, intentFilter)
        val client: SmsRetrieverClient = SmsRetriever.getClient(context)
        val task: Task<Void> = client.startSmsRetriever()
        task.addOnSuccessListener {}
        task.addOnFailureListener {}

        smsReceiver.setListener(this::postValue)
    }

    override fun onInactive() {
        try {
            context.applicationContext.unregisterReceiver(smsReceiver)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.onInactive()
    }
}