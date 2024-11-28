package com.example.mapkit.app.ui.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mapkit.R
import com.example.mapkit.app.model.SearchResult
import com.example.mapkit.databinding.DialogDetailsBottomBinding
import com.example.mapkit.databinding.ItemStarBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailsBottomDialog : BottomSheetDialogFragment() {

    private var listener: Callback? = null
    private var result: SearchResult? = null
    private val binding by viewBinding(DialogDetailsBottomBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_details_bottom, container, false)
    }

//    override fun onStart() {
//        super.onStart()
//        dialog?.let { bottomSheetDialog ->
//            val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
//            bottomSheet?.let { sheet ->
//                val screenHeight = Resources.getSystem().displayMetrics.heightPixels
//                val desiredHeight = (screenHeight * 0.85).toInt()
//                sheet.layoutParams = sheet.layoutParams.apply {
//                    height = desiredHeight
//                }
//            }
//        }
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvTitle.text = result?.name.toString()
        binding.tvDescription.text = result?.address.toString()
        binding.btnClose.setOnClickListener {
            dismiss()
        }

        binding.btnAdd.setOnClickListener {
            result?.let { it1 -> listener?.itemAdd(it1) }
            dismiss()
        }

        for (i in 0 until 5) {
            val imageBinding = ItemStarBinding.inflate(layoutInflater)
            binding.imgRating.addView(imageBinding.root)
            if (i == 4){
                imageBinding.star.setColorFilter(ContextCompat.getColor(requireContext(), R.color.grey_2), android.graphics.PorterDuff.Mode.SRC_IN);
            }
        }

    }



    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener?.onDismiss()
    }

    interface Callback {
        fun itemAdd(item: SearchResult)
        fun itemRemove(item: SearchResult)
        fun onDismiss()
    }

    companion object {
        private val TAG = this::class.simpleName

        fun newInstance(listener: Callback,result: SearchResult): DetailsBottomDialog {
            return DetailsBottomDialog().apply {
                this.listener = listener
                this.result = result
            }
        }
    }
}
