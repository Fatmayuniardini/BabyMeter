package com.capstone.babymeter.ui.history

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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
        val historyItem = historyList[position]
        holder.bind(historyItem)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailsActivity::class.java).apply {
                putExtra("nik", historyItem.nik)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = historyList.size

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.img_item_photo)
        private val nameTextView: TextView = itemView.findViewById(R.id.tv_item_name)
        private val nikTextView: TextView = itemView.findViewById(R.id.tv_item_NIK)

        fun bind(historyItem: HistoryItem) {
            nameTextView.text = historyItem.name
            nikTextView.text = historyItem.nik
            imageView.setImageResource(R.drawable.baby)
        }
    }
}