package com.example.hseobshaga.adapters

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import android.widget.TextView
import com.example.hseobshaga.R


class CustomSpinnerAdapter(context: Context, @LayoutRes private var resource : Int,
                           @IdRes private var textViewResourceId : Int,
                           data : ArrayList<String>) :
                        ArrayAdapter<String>(context, resource, textViewResourceId, data) {

    override fun isEnabled(position: Int): Boolean {
        return position != 0
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = super.getDropDownView(position, convertView, parent)
        val tV = view.findViewById<TextView>(R.id.roomId)
        if (position > 0) {
            tV.setTextColor(Color.BLACK)
        }
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = super.getDropDownView(position, convertView, parent)
        val tV = view.findViewById<TextView>(R.id.roomId)
        if (position == 0){
            tV.setTextColor(Color.GRAY);
        }
        else {
            tV.setTextColor(Color.BLACK);
        }
        return view;
    }

}