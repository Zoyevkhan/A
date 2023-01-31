package com.tv9news.home.activities.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tv9news.R
import com.tv9news.home.activities.interfaces.CustomNavClick
import com.tv9news.models.masterHit.Menus
import com.tv9news.utils.helpers.Helper


class BottomNavAdapter(
    val list: List<Menus>,
    val clickListner: CustomNavClick,
    val context: Context
) : RecyclerView.Adapter<BottomNavAdapter.ViewHolder>() {

    inner class SimpleViewHolder(v: View) : ViewHolder(v) {
        private val imageView = v.findViewById<ImageView>(R.id.imageView)
        private val textView = v.findViewById<TextView>(R.id.textView)
        private val mainLL = v.findViewById<LinearLayout>(R.id.mainLL)
        private val centerLL = v.findViewById<LinearLayout>(R.id.centerLL)
        private val shortIcon = v.findViewById<ImageView>(R.id.shortIcon)

        override fun getBind(position: Int, viewHolder: ViewHolder) {
            val data = list[position]
            if (position == 0) {
                imageView.setColorFilter(ContextCompat.getColor(context, R.color.red))
                textView.setTextColor(ContextCompat.getColor(context, R.color.red))
            }
            textView.text = data.title
            Helper.loadImage(imageView, data.icon, "")
            if (data.title == "center") {
                centerLL.visibility = View.VISIBLE
                mainLL.visibility = View.GONE
                if (list.size == 7) {
                    val params: ViewGroup.LayoutParams = viewHolder.itemView.layoutParams
                    params.width = params.width + 84
                    viewHolder.itemView.requestLayout()
                }
            } else {
                mainLL.visibility = View.VISIBLE
                centerLL.visibility = View.GONE
                if (list.size == 7) {
                    val params: ViewGroup.LayoutParams = viewHolder.itemView.layoutParams
                    params.width = params.width - 14
                    viewHolder.itemView.requestLayout()
                }
            }

            shortIcon.setOnClickListener {
                if (null != clickListner) {
                    clickListner.getBottomClick(data.title, shortIcon)
                }
            }

            mainLL.setOnClickListener {
                if (null != clickListner) {
                    clickListner.getBottomClick(data.title, mainLL)
                }
            }
        }
    }

    abstract class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        abstract fun getBind(position: Int, viewHolder: ViewHolder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.bottom_nav_adapter_item, parent, false)
        view.layoutParams = ViewGroup.LayoutParams(
            parent.width / list.size,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        Log.d("TAGaaa", "parent: " + parent.width / list.size)
        return SimpleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.getBind(position, viewHolder = holder)

    }

    override fun getItemCount(): Int {
        return list.size
    }
}