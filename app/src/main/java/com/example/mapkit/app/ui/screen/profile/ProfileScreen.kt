package com.example.mapkit.app.ui.screen.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mapkit.R
import com.example.mapkit.app.base.BaseFragment
import com.example.mapkit.databinding.ScreenProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileScreen : BaseFragment(R.layout.screen_profile) {

    override val viewModel by viewModels<ProfileScreenVM>()
    private val binding by viewBinding(ScreenProfileBinding::bind)
    private lateinit var mainNavigation: NavController


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainNavigation = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        statusBarColorDark()
        setStatusBarColor(R.color.white)

        viewModel.apply {

        }
    }

}