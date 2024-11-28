package com.example.mapkit.core.ktx

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


inline fun <reified R : RecyclerView> R.onScrollStateChangedX(crossinline callback: (Int) -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            callback(newState)
        }
    })
}


inline fun <reified R : RecyclerView> R.onScrollItemChangedX(
    lm: LinearLayoutManager,
    crossinline callback: (Int) -> Unit
) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                val firstIndex: Int = lm.findFirstCompletelyVisibleItemPosition()
                val lastIndex: Int = lm.findLastCompletelyVisibleItemPosition()
                val middleIndex: Int = (firstIndex + lastIndex) shr 1
                callback(middleIndex)
            }
        }
    })
}