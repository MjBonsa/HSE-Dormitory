package com.example.hseobshaga.adapters.viewtype

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.hseobshaga.R
import com.example.hseobshaga.adapters.base.BaseViewHolder
import com.example.hseobshaga.adapters.base.BaseViewType
import com.example.hseobshaga.adapters.viewholder.UserRequestViewHolder
import com.example.hseobshaga.data.ModelUI
import com.example.hseobshaga.data.UserRequestUI
import com.example.hseobshaga.databinding.ItemUserRequestBinding

class UserRequestViewType(
    private val onItemClick: (UserRequestUI) -> Unit = {}
): BaseViewType <ItemUserRequestBinding, UserRequestUI> {

    override fun isCorrectItem(modelUI: ModelUI): Boolean = modelUI is UserRequestUI

    override fun getLayoutID(): Int = R.layout.item_user_request

    override fun createViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemUserRequestBinding, UserRequestUI> {
        val binding = ItemUserRequestBinding.inflate(layoutInflater, parent, false)
        return UserRequestViewHolder(binding, onItemClick)
    }

    override fun areItemsTheSame(
        oldItem: UserRequestUI,
        newItem: UserRequestUI
    ): Boolean = oldItem.requestName == newItem.requestName && oldItem.timestampFrom == newItem.timestampFrom

    override fun areContentsTheSame(
        oldItem: UserRequestUI,
        newItem: UserRequestUI
    ): Boolean = oldItem == newItem

    override fun getChangePayload(
        oldItem: UserRequestUI,
        newItem: UserRequestUI
    ): Any? = null
}