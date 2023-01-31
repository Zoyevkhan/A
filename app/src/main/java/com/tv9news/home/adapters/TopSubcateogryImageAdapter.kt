package com.tv9news.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tv9news.R
import com.tv9news.models.masterHit.SubCategory
import com.tv9news.utils.helpers.Helper

class TopSubcateogryImageAdapter(val list: List<SubCategory>, val context: Context) :
    RecyclerView.Adapter<TopSubcateogryImageAdapter.ViewHolder>() {

    inner class SimpleViewHolder(v: View) : ViewHolder(v) {
        val titleTv = v.findViewById<TextView>(R.id.titleTv)
        val imageView = v.findViewById<ImageView>(R.id.imageView)

        override fun getBind(position: Int, viewHolder: ViewHolder) {
            val data = list.get(position)
            titleTv.text = data.category_title
            if (data.category_logo != null) {
                Helper.loadImage(imageView, data.category_logo, "")
            }

            itemView.setOnClickListener {
                titleTv.setTextColor(context.resources.getColor(R.color.red))
                imageView.background = context.resources.getDrawable(R.drawable.ic_top_category_image_bg)
            }
        }
    }

    abstract class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        abstract fun getBind(position: Int, viewHolder: ViewHolder)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.top_subcategory_item_image, parent, false)
        return SimpleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.getBind(position, viewHolder = holder)

    }

    override fun getItemCount(): Int {
        return list.size
    }
}