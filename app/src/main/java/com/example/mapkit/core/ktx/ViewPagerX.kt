package com.example.mapkit.core.ktx


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import androidx.viewpager2.widget.ViewPager2


fun ViewPager2.findCurrentFragment(fragmentManager: FragmentManager): Fragment? {
    return fragmentManager.findFragmentByTag("f$currentItem")
}

fun ViewPager2.findFragmentAtPosition(
    fragmentManager: FragmentManager,
    position: Int
): Fragment? {
    return fragmentManager.findFragmentByTag("f$position")
}

inline fun ViewPager.doOnPageSelected(
    crossinline action: (
        position: Int
    ) -> Unit
): OnPageChangeListener = addOnPageChangeListener(onPageSelectedX = action)


inline fun ViewPager.doOnPageScrolled(
    crossinline onPageScrolledX: (
        position: Int,
        positionOffset: Float,
        positionOffsetPixels: Int
    ) -> Unit
): OnPageChangeListener = addOnPageChangeListener(onPageScrolledX = onPageScrolledX)


inline fun ViewPager.addOnPageChangeListener(
    crossinline onPageScrolledX: (
        position: Int,
        positionOffset: Float,
        positionOffsetPixels: Int
    ) -> Unit = { _, _, _ -> },
    crossinline onPageSelectedX: (
        position: Int
    ) -> Unit = {},
    crossinline onPageScrollStateChangedX: (
        state: Int,
    ) -> Unit = {}
): OnPageChangeListener {
    val onPageChangeListener = object : OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            onPageScrolledX.invoke(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            onPageSelectedX.invoke(position)
        }

        override fun onPageScrollStateChanged(state: Int) {
            onPageScrollStateChangedX.invoke(state)
        }
    }
    addOnPageChangeListener(onPageChangeListener)
    return onPageChangeListener
}


inline fun ViewPager2.doOnPageSelected(
    crossinline action: (
        position: Int
    ) -> Unit
): ViewPager2.OnPageChangeCallback = addOnPageChangeListener(onPageSelectedX = action)


inline fun ViewPager2.addOnPageChangeListener(
    crossinline onPageScrolledX: (
        position: Int,
        positionOffset: Float,
        positionOffsetPixels: Int
    ) -> Unit = { _, _, _ -> },
    crossinline onPageSelectedX: (
        position: Int
    ) -> Unit = {},
    crossinline onPageScrollStateChangedX: (
        state: Int,
    ) -> Unit = {}
): ViewPager2.OnPageChangeCallback {
    val onPageChangeListener = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            onPageSelectedX.invoke(position)
        }
    }
    registerOnPageChangeCallback(onPageChangeListener)
    return onPageChangeListener
}