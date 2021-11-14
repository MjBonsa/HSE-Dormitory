package com.example.hseobshaga.adapters.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.hseobshaga.data.ModelUI

abstract class BaseViewHolder <B: ViewBinding, E: ModelUI>(
    binding: B
): RecyclerView.ViewHolder(binding.root) {

    abstract fun bind(modelUI: E)
    open fun bind(modelUI: E, payloads: List<Any>) {}
}