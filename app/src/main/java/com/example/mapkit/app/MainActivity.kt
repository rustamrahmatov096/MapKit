package com.example.mapkit.app

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.example.mapkit.R
import com.example.mapkit.app.base.BaseActivity
import com.example.mapkit.app.util.LocaleHelper
import com.example.mapkit.core.ktx.gone
import com.example.mapkit.core.ktx.visible
import com.example.mapkit.core.model.ThemeType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity(R.layout.activity_main) {

    private val viewModel: MainActivityVM by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        try {
            LocaleHelper.onAttach(this)
        } catch (_: Exception) {

        }

        when (viewModel.getCurrentTheme()) {
            ThemeType.System -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            ThemeType.Night -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            ThemeType.Light -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }

    }





    fun showLoading() {
        findViewById<View>(R.id.layout_loading).visible()
    }

    fun hideLoading() {
        findViewById<View>(R.id.layout_loading).gone()
    }

}