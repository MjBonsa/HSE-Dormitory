package com.example.hseobshaga.adapters

import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.example.hseobshaga.adapters.base.BaseViewType
import com.example.hseobshaga.data.ModelUI

class ItemViewTypeDiffUtil(
    private val viewTypes: List<BaseViewType<*, *>>
): DiffUtil.ItemCallback<ModelUI>() {

    override fun areItemsTheSame(oldItem: ModelUI, newItem: ModelUI): Boolean {
        if (oldItem::class != newItem::class) return false
        return getModelViewType(oldItem).areItemsTheSame(oldItem, newItem)
    }

    override fun areContentsTheSame(
        oldItem: ModelUI,
        newItem: ModelUI
    ): Boolean {
        if (oldItem::class != newItem::class) return false
        return getModelViewType(oldItem).areContentsTheSame(oldItem, newItem)
    }

    override fun getChangePayload(oldItem: ModelUI, newItem: ModelUI): Any? {
        if (oldItem::class != newItem::class) return false
        return getModelViewType(oldItem).getChangePayload(oldItem, newItem)
    }

    private fun getModelViewType(
        modelUI: ModelUI
    ): BaseViewType<ViewBinding, ModelUI> = viewTypes.find {
        it.isCorrectItem(modelUI)
    }.let { it as BaseViewType<ViewBinding, ModelUI> }
}