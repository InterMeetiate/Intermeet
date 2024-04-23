package com.intermeet.android

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.intermeet.android.R

class CustomAdapter(context: Context, private val data: ArrayList<String>) : ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, data) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.custom_list_item, parent, false)

        val textView = view.findViewById<TextView>(R.id.textView)
        val deleteButton = view.findViewById<ImageView>(R.id.deleteButton)

        textView.text = data[position]

        deleteButton.setOnClickListener {
            data.removeAt(position)
            notifyDataSetChanged()
        }

        return view
    }
}