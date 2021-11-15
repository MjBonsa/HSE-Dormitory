package com.example.hseobshaga.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.hseobshaga.adapters.base.BaseViewHolder
import com.example.hseobshaga.adapters.base.BaseViewType
import com.example.hseobshaga.data.ModelUI
import java.lang.IllegalStateException

class DelegateAdapter(
    private val viewTypes: List<BaseViewType<*, *>>
): ListAdapter<ModelUI, BaseViewHolder<ViewBinding, ModelUI>>(
    ItemViewTypeDiffUtil(viewTypes = viewTypes)
) {

    private val items: MutableList<ModelUI> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewBinding, ModelUI> {
        val inflater = LayoutInflater.from(parent.context)
        return viewTypes.find { it.getLayoutID() == viewType }
            ?.createViewHolder(inflater, parent)
            ?.let { it as BaseViewHolder<ViewBinding, ModelUI> } ?: throw IllegalStateException()
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<ViewBinding, ModelUI>,
        position: Int
    ) {
        holder.bind(items[position])
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<ViewBinding, ModelUI>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if(payloads.isNullOrEmpty()) super.onBindViewHolder(holder, position, payloads)
        else holder.bind(items[position], payloads)
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int = viewTypes.find {
        it.isCorrectItem(items[position])
    }?.getLayoutID() ?: throw IllegalStateException()

    fun update() {
        submitList(items.toMutableList())
    }

    fun setItems(newItems: List<ModelUI>) {
        items.clear()
        items.addAll(newItems)
        update()
    }
}