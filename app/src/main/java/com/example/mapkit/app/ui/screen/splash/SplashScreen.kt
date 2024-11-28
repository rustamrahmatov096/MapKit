package com.example.mapkit.app.ui.screen.splash

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mapkit.R
import com.example.mapkit.app.base.BaseFragment
import com.example.mapkit.core.ktx.visible
import com.example.mapkit.databinding.ScreenSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashScreen : BaseFragment(R.layout.screen_splash) {

    override val viewModel by viewModels<SplashScreenVM>()
    private val binding by viewBinding(ScreenSplashBinding::bind)
    private var animSlideDown: Animation? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarColor(R.color.white)
        statusBarColorDark()
        viewModel.apply {
            homeScreenLiveData.observe(viewLifecycleOwner, homeScreenObserver)
            loginScreenLiveData.observe(viewLifecycleOwner, loginScreenObserver)
        }
        startAnim()
    }

    private fun startAnim() {
        viewLifecycleOwner.lifecycleScope.launch {
            animSlideDown = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_sown_anim)
            binding.imgSplash.startAnimation(animSlideDown)
            binding.imgSplash.visible()
            delay(800)
            viewModel.nextScreen()
        }
    }

    private val homeScreenObserver = Observer<Unit> {
        findNavController().navigate(R.id.mainScreen)
    }

    private val loginScreenObserver = Observer<Unit> {

    }
}