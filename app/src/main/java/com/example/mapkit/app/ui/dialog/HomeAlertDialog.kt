package com.example.mapkit.app.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mapkit.R
import com.example.mapkit.app.model.SearchResult
import com.example.mapkit.databinding.DialogHomeAlertBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeAlertDialog : DialogFragment() {

    private var result: SearchResult? = null
    private var listener: Callback? =null


    private val binding by viewBinding(DialogHomeAlertBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_home_alert, container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let { window ->
            val params = window.attributes
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT // Optional: Adjust height if needed
            window.attributes = params
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editName.setText(result?.address.toString())

        binding.editName.doAfterTextChanged {
            result?.address = it.toString()
        }

        binding.btnCancel.setOnClickListener { dismiss() }
        binding.btnConfirm.setOnClickListener {
            result?.let { it1 -> listener?.insertResult(it1) }
            dismiss()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    fun show(manager: FragmentManager) {
        if (manager.findFragmentByTag(TAG) == null) show(manager, TAG)
    }

    companion object Factory {

        private val TAG = this::class.simpleName

        @JvmStatic
        fun newInstance(
            listener:Callback? = null,
            result: SearchResult? = null
        ): HomeAlertDialog = HomeAlertDialog().apply {
            this.result = result
            this.listener = listener
        }

    }

    interface Callback {
        fun insertResult(item:SearchResult)
    }
}