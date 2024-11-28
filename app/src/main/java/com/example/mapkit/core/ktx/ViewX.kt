package com.example.mapkit.core.ktx

import android.util.TypedValue
import android.view.View
import androidx.annotation.DrawableRes
import coil.Coil
import coil.ImageLoader
import coil.request.Disposable
import coil.request.ImageRequest
import com.google.android.material.snackbar.Snackbar


fun View.addRipple() = with(TypedValue()) {
    context.theme.resolveAttribute(android.R.attr.selectableItemBackground, this, true)
    setBackgroundResource(resourceId)
}

fun View.addCircleRipple() = with(TypedValue()) {
    context.theme.resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, this, true)
    setBackgroundResource(resourceId)
}


fun View.gone() {
    if (visibility != View.GONE) visibility = View.GONE
}

fun View.visible() {
    if (visibility != View.VISIBLE) visibility = View.VISIBLE
}

fun View.invisible() {
    if (visibility != View.INVISIBLE) visibility = View.INVISIBLE
}



@JvmSynthetic
inline fun View.setBackgroundCoil(
    @DrawableRes drawableResId: Int,
    imageLoader: ImageLoader = Coil.imageLoader(context),
    builder: ImageRequest.Builder.() -> Unit = {}
): Disposable = setBackgroundAny(drawableResId, imageLoader, builder)


@JvmSynthetic
inline fun View.setBackgroundAny(
    data: Any?,
    imageLoader: ImageLoader = Coil.imageLoader(context),
    builder: ImageRequest.Builder.() -> Unit = {}
): Disposable {
    val request = ImageRequest.Builder(context)
        .data(data)
        .target {
            background = it
        }
        .apply(builder)
        .build()
    return imageLoader.enqueue(request)
}

inline fun <reified V : View> V.snackbar(msg: String, body: Snackbar.() -> Unit = {}): Snackbar {
    return Snackbar.make(this, msg, Snackbar.LENGTH_SHORT).apply(body).apply {
        show()
    }
}