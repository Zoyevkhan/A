package com.tv9news.home.activities.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tv9news.R
import com.tv9news.home.activities.interfaces.CustomNavClick
import com.tv9news.models.masterHit.Menus
import com.tv9news.utils.helpers.Helper

class DrawerItemAdapter(val list: List<Menus>, val clickListner: CustomNavClick) :
    RecyclerView.Adapter<DrawerItemAdapter.ViewHolder>() {

    inner class SimpleViewHolder(v: View) : ViewHolder(v) {
        val imageView = v.findViewById<ImageView>(R.id.imageView)
        val textView = v.findViewById<TextView>(R.id.textView)

        init {
            itemView.setOnClickListener {
                clickListner.getCategoris(list[layoutPosition].title)
            }
        }

        override fun getBind(position: Int, viewHolder: ViewHolder) {
            val data = list[position]
            textView.setText(data.title)
            Helper.loadImage(imageView, data.icon, "")

        }
    }

    abstract class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        abstract fun getBind(position: Int, viewHolder: ViewHolder)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return SimpleViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.drawer_category_adapter_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.getBind(position, viewHolder = holder)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}