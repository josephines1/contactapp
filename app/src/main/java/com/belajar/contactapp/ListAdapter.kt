package com.belajar.contactapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListAdapter(val contact: List<Contact>) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tv_item_name)
        val address: TextView = itemView.findViewById(R.id.tv_item_address)
        val number: TextView = itemView.findViewById(R.id.tv_item_number)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view, parent, false)

        return ViewHolder(view);
    }

    override fun getItemCount() = contact.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = contact[position]

        holder.name.text = item.name
        holder.address.text = item.address
        holder.number.text = item.number
    }

}