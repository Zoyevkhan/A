package com.tv9news.home.activities.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tv9news.R
import com.tv9news.home.interfaces.HomeItemClick
import com.tv9news.models.home.Lists

class GridTrendItemAdapter(private val mItems: List<Lists>) :
    RecyclerView.Adapter<GridTrendItemAdapter.DivItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DivItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.grid_trend_topic_item_child, parent, false)
        return DivItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: DivItemViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    inner class DivItemViewHolder constructor(itemView: View) : RecyclerView.ViewHolder
        (itemView) {
        val itemContainer = itemView.findViewById<ConstraintLayout>(R.id.itemContainer)
        private val text: TextView = itemView.findViewById(R.id.text)

        fun onBind(position: Int) {
            val data = mItems.get(position)
            text.text = "#" + mItems[position].tag

            itemContainer.setOnClickListener {

            }
        }
    }
}