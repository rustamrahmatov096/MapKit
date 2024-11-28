package com.example.mapkit.app.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView


abstract class SuperListAdapter<T>(
    private val viewId: Int,
    diffUtil: DiffUtil.ItemCallback<T>
) : ListAdapter<T, SuperListAdapter<T>.MyHolder>(diffUtil) {

    constructor(
        viewId: Int,
        areItemsTheSame: (oldItem: T, newItem: T) -> Boolean,
        areContentsTheSame: (oldItem: T, newItem: T) -> Boolean
    ) : this(viewId, object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T & Any, newItem: T & Any): Boolean {
            return areItemsTheSame(oldItem, newItem)
        }

        override fun areContentsTheSame(oldItem: T & Any, newItem: T & Any): Boolean {
            return areContentsTheSame(oldItem, newItem)
        }
    })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyHolder(inflater.inflate(viewId, parent, false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bindHolder()
    }

    inner class MyHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bindHolder() {
            bind(getItem(absoluteAdapterPosition), view, absoluteAdapterPosition)
        }
    }

    abstract fun bind(t: T, view: View, adapterPosition: Int)
}