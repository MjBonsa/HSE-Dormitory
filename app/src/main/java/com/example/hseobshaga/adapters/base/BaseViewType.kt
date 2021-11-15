package com.example.hseobshaga.adapters.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.example.hseobshaga.data.ModelUI

interface BaseViewType <B: ViewBinding, E: ModelUI> {

    fun isCorrectItem(modelUI: ModelUI): Boolean
    fun getLayoutID(): Int
    fun createViewHolder(layoutInflater: LayoutInflater, parent: ViewGroup): BaseViewHolder<B, E>
    fun areItemsTheSame(oldItem: E, newItem: E): Boolean
    fun areContentsTheSame(oldItem: E, newItem: E): Boolean
    fun getChangePayload(oldItem: E, newItem: E): Any?
}