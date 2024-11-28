package com.example.mapkit.core.ktx

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.view.View
import com.example.mapkit.core.ktx.context.startPdfActivity
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL


fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun Long?.toAllSum(): String {
    if (this == null) return "0.00"
    val a = this / 100
    val toSumString = a.toSumString()
    val toFractionalPart = this.toFractionalPart(2)
    return "$toSumString.$toFractionalPart"
}

fun Double?.toAllSum(): String {
    if (this == null) return "0.00"
    val a = (this / 100).toLong()
    val toSumString = a.toSumString()
    val toFractionalPart = this.toFractionalPart(2)
    return "$toSumString.$toFractionalPart"
}

fun Int?.toAllSum(): String {
    if (this == null) return "0.00"
    val a = (this / 100).toLong()
    val toSumString = a.toSumString()
    val toFractionalPart = this.toLong().toFractionalPart(2)
    return "$toSumString.$toFractionalPart"
}

fun downloadPdfFile(ctx: Context, url: String, file: File) {


    if (!file.isFile) {

        val dm: DownloadManager? = ctx.getSystemService(DOWNLOAD_SERVICE) as DownloadManager?
        val request: DownloadManager.Request = DownloadManager.Request(Uri.parse(url))

        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationUri(Uri.fromFile(file))
        val receiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                ctx.unregisterReceiver(this)
                ctx.startPdfActivity(file.absolutePath)
            }
        }

        ctx.registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        dm?.enqueue(request)


    } else {
        ctx.startPdfActivity(file.absolutePath)
    }

}

fun downloadFile(
    context: Context,
    url: URL, fileName: String,
): String? = try {

    url.openStream().use { inp ->

        BufferedInputStream(inp).use { bis ->
            val dirPath = context.getExternalFilesDir(null)!!.absolutePath
            val dir = File(dirPath)
            if (!dir.exists()) dir.mkdirs()

            val fileAddress = dirPath.plus("/${fileName}.pdf")
            val pdf = File(fileAddress)

            FileOutputStream(pdf).use { fos ->
                val data = ByteArray(1024)
                var count: Int
                while (bis.read(data, 0, 1024).also { count = it } != -1) {
                    fos.write(data, 0, count)
                }
                fos.flush()
                fos.close()
                pdf.absolutePath
            }
        }
    }
} catch (e: Exception) {
    null
}