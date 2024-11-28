package com.example.mapkit.app.base

import NetworkChangeReceiver
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.example.mapkit.R
import com.example.mapkit.app.MainActivity
import com.example.mapkit.core.ktx.alertDialog


abstract class BaseFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {
    abstract val viewModel: BaseVM
    private lateinit var mainNavigation: NavController
    private lateinit var networkChangeReceiver: NetworkChangeReceiver



    val navAnimOptions = NavOptions.Builder().setEnterAnim(R.anim.activity_open_from_right_to_left)
        .setExitAnim(R.anim.activity_close_from_right_to_left)
        .setPopEnterAnim(R.anim.activity_open_from_left_to_right)
        .setPopExitAnim(R.anim.activity_close_from_left_to_right).build()

    val navAnimScrollUp = NavOptions.Builder().setEnterAnim(R.anim.dialog_open_from_bottom_to_top)
        .setPopExitAnim(R.anim.dialog_close_from_top_to_bottom).build()

    override fun onStart() {
        super.onStart()
        networkChangeReceiver = NetworkChangeReceiver(childFragmentManager)
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        activity?.registerReceiver(networkChangeReceiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()
        activity?.unregisterReceiver(networkChangeReceiver)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainNavigation = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        viewModel.apply {
            globalErrorLiveData.observe(viewLifecycleOwner, globalErrorObserver)
            logoutScreenLiveData.observe(viewLifecycleOwner, logoutScreenObserver)
            needUpdateScreenLiveData.observe(viewLifecycleOwner, needUpdateScreenObserver)
            noConnection.observe(viewLifecycleOwner, noConnectionObserver)
        }
    }

    private val noConnectionObserver = Observer<Unit> {
        try {
//            noConnectionDialog.show(requireActivity().supportFragmentManager)
//            toast("No Internet")
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    val globalErrorObserver = Observer<String> {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.lbl_error)).setMessage(it)
            .setIcon(R.drawable.ic_error_outline)
            .setPositiveButton(getString(R.string.lbl_ok)) { dialog, _ ->
                dialog.dismiss()
            }
        builder.show()
    }

    private val logoutScreenObserver = Observer<Unit> {

    }

    private val needUpdateScreenObserver = Observer<String> {

    }

    protected fun statusBarColorLight() {
        statusBarColor(false)
    }

    protected fun statusBarColorDark() {
        statusBarColor(true)
    }

    fun showYesNoDialog(question: String, onPositive: (() -> Unit)) {
        alertDialog().setTitle(getString(R.string.lbl_info)).setMessage(question)
            .setIcon(R.drawable.ic_xazna_business)
            .setPositiveButton(resources.getString(R.string.lbl_yes)) { dialog, _ ->
                onPositive.invoke()
                dialog.dismiss()
            }.setNegativeButton(
                resources.getString(R.string.lbl_no)
            ) { p0, p1 -> }.show()
    }

    fun showInfoDialog(message: String) {
        alertDialog().setTitle(getString(R.string.lbl_info)).setMessage(message)
            .setIcon(R.drawable.ic_xazna_business)
            .setPositiveButton(resources.getString(R.string.lbl_ok)) { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    fun showSuccessDialogAndGoHome(content: String, actionOK: (() -> Unit)) {
        alertDialog().setMessage(content).setTitle(getString(R.string.lbl_info))
            .setIcon(R.drawable.ic_xazna_business)
            .setPositiveButton(resources.getString(R.string.lbl_ok)) { dialog, _ ->
                actionOK.invoke()
                dialog.dismiss()
            }.show()
    }

    private fun statusBarColor(boolean: Boolean) {
        val window = requireActivity().window
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, requireView()).isAppearanceLightStatusBars = boolean
    }

    protected fun setStatusBarColor(color: Int) {
        val window: Window = requireActivity().window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(color)
    }

    protected fun hideSystemUI() {
        val window = requireActivity().window
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, requireView()).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    protected fun showSystemUI() {
        val window = requireActivity().window
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(
            window, requireView()
        ).show(WindowInsetsCompat.Type.systemBars())
    }

    protected fun showKeyboard() {
        val imm: InputMethodManager? =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        val focus = activity?.currentFocus
        if (focus != null) imm?.hideSoftInputFromWindow(
            focus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    open fun hideKeyboard() {
        val activity = requireActivity()
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) view = View(activity)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showGlobalLoading() {
        (requireActivity() as MainActivity).showLoading()
    }

    fun hideGlobalLoading() {
        (requireActivity() as MainActivity).hideLoading()
    }

    override fun onDestroy() {
        super.onDestroy()
        hideGlobalLoading()
    }
}