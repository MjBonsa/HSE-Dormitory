package com.example.hseobshaga.adapters

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.hseobshaga.R


class CustomListViewAdapter(private val context: Activity, private val data: ArrayList<String>)
    : ArrayAdapter<String>(context, R.layout.item_user_neighbours, data) {
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.item_user_neighbours, null, true)
        val titleText = rowView.findViewById(R.id.fullNameTV) as TextView
        titleText.text = data[position]
        return rowView
    }
}