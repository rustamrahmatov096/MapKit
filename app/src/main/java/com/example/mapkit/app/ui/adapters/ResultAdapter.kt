package com.example.mapkit.app.ui.adapters

import android.view.View
import com.example.mapkit.R
import com.example.mapkit.app.base.SuperListAdapter
import com.example.mapkit.app.model.DetailsModel
import com.example.mapkit.app.model.SearchResult
import com.example.mapkit.databinding.ItemResultListBinding

class ResultAdapter(
    private val listener: Callback
) : SuperListAdapter<SearchResult>(
    R.layout.item_result_list,
    { oldItem, newItem -> oldItem == newItem },
    { oldItem, newItem -> oldItem == newItem },
) {

    override fun bind(t: SearchResult, view: View, adapterPosition: Int) {
        val binding = ItemResultListBinding.bind(view)

        binding.tvName.text = t.name
        binding.tvAddress.text = t.address

        binding.cardItem.setOnClickListener {
            listener.itemSelect(t)
        }

    }

    interface Callback {
        fun itemSelect(item: SearchResult)
    }
}