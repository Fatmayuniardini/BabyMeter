package com.capstone.babymeter.history

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.babymeter.R

class HistoryAdapter(
    private val historyList: List<HistoryItem>,
    private val context: Context
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = historyList[position]
        holder.name.text = item.name
        holder.description.text = item.nik // Sesuaikan dengan yang ingin Anda tampilkan di deskripsi
        Glide.with(context).load(item.imageUrl).into(holder.photo)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailsActivity::class.java).apply {
                putExtra("item", item)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = historyList.size

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tv_item_name)
        val description: TextView = itemView.findViewById(R.id.tv_item_description)
        val photo: ImageView = itemView.findViewById(R.id.img_item_photo)

    }
}