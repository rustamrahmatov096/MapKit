package com.example.mapkit.app.ui.screen.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mapkit.R
import com.example.mapkit.app.MainActivityVM
import com.example.mapkit.app.base.BaseFragment
import com.example.mapkit.databinding.ScreenMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainScreen : BaseFragment(R.layout.screen_main) {
    override val viewModel: MainScreenVM by viewModels()
    private val binding by viewBinding(ScreenMainBinding::bind)

    private val mainViewModel by activityViewModels<MainActivityVM>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        statusBarColorLight()

        binding.bottomNavigation.setupWithNavController(
            Navigation.findNavController(
                requireActivity(),
                R.id.navHostScreen
            )
        )

        mainViewModel.apply {
//            openOperationLiveData.observe(viewLifecycleOwner, openOperationObserver)
        }
    }

    override fun onResume() {
        super.onResume()
        val intentNew = requireActivity().intent

        if (intentNew != null && intentNew.data != null) {

            val uri = intentNew.data!!.toString()

            return
        }
    }
}