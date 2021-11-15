package com.example.hseobshaga.adapters.viewholder

import android.util.Log
import com.example.hseobshaga.adapters.base.BaseViewHolder
import com.example.hseobshaga.data.UserRequestUI
import com.example.hseobshaga.databinding.ItemUserRequestBinding
import com.example.hseobshaga.ext.convertToDate

class UserRequestViewHolder(
    private val binding: ItemUserRequestBinding,
    private val onItemClick: (UserRequestUI) -> Unit = {}
): BaseViewHolder<ItemUserRequestBinding, UserRequestUI>(binding) {

    override fun bind(modelUI: UserRequestUI) {
        binding.apply {
            requestName.text = modelUI.requestName
            estimationTimeOfCompletion.text =
                "${modelUI.timestampFrom.convertToDate()} - ${modelUI.timestampTo.convertToDate()}" //Лучше чтобы в модельке User был уже удобно читаемый формат
            root.setOnClickListener {
                onItemClick(modelUI)
            }
        }
    }
}