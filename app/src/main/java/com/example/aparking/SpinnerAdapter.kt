package com.example.aparking

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

data class Item(val flagResource: Int, val language: String)

class SpinnerAdapter(context: Context, resource: Int, items: List<Item>)
    : ArrayAdapter<Item>(context, resource, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    private fun createItemView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false)

        val flagImageView = view.findViewById<ImageView>(R.id.flag_image)
        val languageTextView = view.findViewById<TextView>(R.id.language_text)

        val item = getItem(position)
        if (item != null) {
            flagImageView.setImageResource(item.flagResource)
        }
        if (item != null) {
            languageTextView.setText(item.language)
        }

        return view
    }
}
