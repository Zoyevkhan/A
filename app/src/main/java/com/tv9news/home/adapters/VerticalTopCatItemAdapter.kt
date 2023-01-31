package com.tv9news.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tv9news.R
import com.tv9news.home.interfaces.HomeItemClick
import com.tv9news.models.home.SubCategory

class VerticalTopCatItemAdapter(
    val list: List<SubCategory>,
    val context: Context,
    val homeItemClick: HomeItemClick
) :
    RecyclerView.Adapter<VerticalTopCatItemAdapter.ViewHolder>() {
    private var globalPosition = 0

    inner class SimpleViewHolder(v: View) : ViewHolder(v) {
        val itemContainer = v.findViewById<RelativeLayout>(R.id.itemContainer)
        val titleTv = v.findViewById<TextView>(R.id.titleTv)
        val view = v.findViewById<View>(R.id.view)
        override fun getBind(position: Int, viewHolder: ViewHolder) {
            val data = list.get(position)
            titleTv.text = data.category
            if (position == globalPosition){
                titleTv.setTextColor(context.resources.getColor(R.color.black_text))
                view.visibility = View.VISIBLE
            }else{
                titleTv.setTextColor(context.resources.getColor(R.color.app_text_color))
                view.visibility = View.GONE
            }
            itemView.setOnClickListener {
                if (null != homeItemClick) {
                    homeItemClick.itemHomeSubCatClick(data)
                }
                globalPosition = position
                notifyDataSetChanged()
            }
        }
    }

    abstract class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        abstract fun getBind(position: Int, viewHolder: ViewHolder)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.vertical_cat_details_top_item_child, parent, false);
        return SimpleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.getBind(position, viewHolder = holder)

    }

    override fun getItemCount(): Int {
        return list.size
    }
}