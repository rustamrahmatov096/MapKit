package com.example.mapkit.app.ui.dialog

import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mapkit.R
import com.example.mapkit.app.model.SearchResult
import com.example.mapkit.app.ui.adapters.SearchResultAdapter
import com.example.mapkit.core.ktx.gone
import com.example.mapkit.core.ktx.visible
import com.example.mapkit.databinding.DialogSearchBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchBottomDialog : BottomSheetDialogFragment(), (SearchResult) -> Unit {

    private var listener: Callback? = null
    private var adapter: SearchResultAdapter? = null
    private val binding by viewBinding(DialogSearchBottomBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_search_bottom, container, false)
    }

    override fun onStart() {
        super.onStart()

        dialog?.let { bottomSheetDialog ->
            val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let { sheet ->
                val screenHeight = Resources.getSystem().displayMetrics.heightPixels
                val desiredHeight = (screenHeight * 0.85).toInt()
                sheet.layoutParams = sheet.layoutParams.apply {
                    height = desiredHeight
                }
            }

            // Force the keyboard to show
            bottomSheetDialog.window?.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        // Focus the EditText and show the keyboard with a slight delay
        binding.editSearch.post {
            binding.editSearch.requestFocus()
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.editSearch, InputMethodManager.SHOW_IMPLICIT)
        }

        // Handle text changes in the search field
        binding.editSearch.doAfterTextChanged { text ->
            if (!text.isNullOrEmpty()) {
                binding.btnClear.visible()
                listener?.searchText(text.toString()) // Notify listener for search updates
            } else {
                binding.btnClear.gone()
                updateSearchResults(emptyList()) // Clear results if search is empty
            }
        }

        // Clear the search input
        binding.btnClear.setOnClickListener {
            binding.editSearch.setText("")
        }
    }

    private fun setupRecyclerView() {
        adapter = SearchResultAdapter(emptyList(), this)
        binding.recyclerResult.adapter = adapter
        binding.recyclerResult.layoutManager = LinearLayoutManager(context)
    }

    fun updateSearchResults(results: List<SearchResult>) {
        adapter?.updateData(results) // Update adapter data
    }

    override fun invoke(result: SearchResult) {
        listener?.itemClicked(result) // Pass selected result to listener
        dismiss()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener?.onDismiss()
    }

    interface Callback {
        fun itemClicked(item: SearchResult)
        fun searchText(query: String)
        fun onDismiss()
    }

    companion object {
        private val TAG = this::class.simpleName

        fun newInstance(listener: Callback): SearchBottomDialog {
            return SearchBottomDialog().apply {
                this.listener = listener
            }
        }
    }
}
