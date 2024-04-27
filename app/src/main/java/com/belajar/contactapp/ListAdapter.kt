package com.belajar.contactapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.widget.ImageView
import com.belajar.contactapp.model.Contact
import com.squareup.picasso.Picasso

class ListAdapter(private val context: Context, val contact: List<Contact>) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tv_item_name)
        val imageView: ImageView = itemView.findViewById(R.id.iv_contact_image)
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

        // Load the image URL into the ImageView using Picasso
        Picasso.get()
            .load(item.imageUrl)
            .into(holder.imageView)

        if(item.imageUrl == null) {
            holder.imageView.setImageResource(R.drawable.account_circle_24)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("CONTACT", item)
            context.startActivity(intent)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("CONTACT", item)
            context.startActivity(intent)
        }
    }
}