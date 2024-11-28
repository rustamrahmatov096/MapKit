package com.example.mapkit.app.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mapkit.R
import com.example.mapkit.core.ktx.isOnline
import com.example.mapkit.databinding.DialogNoConnectionBottomBinding


class NoConnectionBottomDialog : DialogFragment() {

    private val binding by viewBinding(DialogNoConnectionBottomBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_no_connection_bottom, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.setCancelable(false)

        showNoNetworkLottie()

        if (requireContext().isOnline()) {
            dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    fun show(manager: FragmentManager) {
        if (manager.findFragmentByTag(TAG) == null) showNow(manager, TAG)
    }

    companion object Factory {
        private val TAG = this::class.simpleName
    }

    fun updateNetworkState(isConnected: Boolean) {
        if (isConnected) {
            showHasInternetLottie()
        } else {
            showNoNetworkLottie()
        }
    }

    private fun showNoNetworkLottie() {
        if (isAdded && view != null) {
            binding.lottieAnimationView.clearAnimation()
            binding.lottieAnimationView.setAnimation("not_connected.json")
            binding.lottieAnimationView.playAnimation()
        }
    }

    private fun showHasInternetLottie() {
        if (isAdded && view != null) {
            binding.lottieAnimationView.setAnimation("has_internet.json")
            binding.lottieAnimationView.playAnimation()
            binding.lottieAnimationView.postDelayed({
                if (isAdded && view != null) { // Check again inside postDelayed
                    binding.lottieAnimationView.clearAnimation()
                    binding.lottieAnimationView.setAnimation("not_connected.json")
                    dialog?.dismiss()
                }
            }, 1500)
        }
    }
}
