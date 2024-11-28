package com.example.mapkit.app.ui.screen.home

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mapkit.R
import com.example.mapkit.app.base.BaseFragment
import com.example.mapkit.app.model.SearchResult
import com.example.mapkit.app.ui.adapters.ResultAdapter
import com.example.mapkit.app.ui.dialog.DeleteBottomDialog
import com.example.mapkit.databinding.ScreenHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeScreen : BaseFragment(R.layout.screen_home), ResultAdapter.Callback,
    DeleteBottomDialog.Callback {

    override val viewModel by viewModels<HomeScreenVM>()
    private val binding by viewBinding(ScreenHomeBinding::bind)
    private lateinit var mainNavigation: NavController
    private val adapter by lazy { ResultAdapter(this) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainNavigation = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        statusBarColorDark()
        setStatusBarColor(R.color.white_light)
        binding.recyclerResult.adapter = adapter

        viewModel.apply {
            resultLiveData.observe(viewLifecycleOwner,resultObserver)
        }
    }

    private val resultObserver = Observer<List<SearchResult>>{
        adapter.submitList(it)

        binding.imgEmpty.isVisible = it.isEmpty()
    }

    override fun itemSelect(item: SearchResult) {
        DeleteBottomDialog.newInstance(this,item).show(childFragmentManager,DeleteBottomDialog::class.simpleName)
    }

    override fun itemAdd(item: SearchResult) {

    }

    override fun itemRemove(item: SearchResult) {
        viewModel.delete(item)
    }

    override fun onDismiss() {

    }

}