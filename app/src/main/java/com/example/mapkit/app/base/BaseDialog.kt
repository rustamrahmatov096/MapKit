package com.example.mapkit.app.base

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog


abstract class BaseDialog(context: Context?, val resId: Int) /*: LifecycleObserver*/ {
    protected val view = LayoutInflater.from(context).inflate(resId, null, false) as ViewGroup
    protected val dialog = context?.let { AlertDialog.Builder(it).setView(view).setCancelable(false).create() }
    private var onClose: (() -> Unit)? = null

    init {
//        dialog?.window?.setWindowAnimations(R.style.DialogAnimationTopToBottom)
    }

    open fun show() {
        dialog?.show()
    }


    fun close(change: Boolean = false) {
        dialog?.dismiss()
        if (change) onClose?.invoke()
    }

    fun setOnCloseListener(f: () -> Unit) {
        onClose = f
    }
}

typealias Listener = (() -> Unit)