package com.tv9news.home.activities.adapters

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
import com.tv9news.models.home.Lists
import com.tv9news.utils.helpers.Helper
import de.hdodenhof.circleimageview.CircleImageView

class HorizontalOpinionItemAdapter(
    val list: List<Lists>,
    val context: Context,
    val homeItemClick: HomeItemClick
) :
    RecyclerView.Adapter<HorizontalOpinionItemAdapter.ViewHolder>() {

    inner class SimpleViewHolder(v: View) : ViewHolder(v) {
        val titleTv = v.findViewById<TextView>(R.id.titleTv)
        val userNameTv = v.findViewById<TextView>(R.id.userNameTv)
        val userImageView = v.findViewById<CircleImageView>(R.id.userImageView)
        val itemContainer = v.findViewById<RelativeLayout>(R.id.itemContainer)

        override fun getBind(position: Int, viewHolder: ViewHolder) {
            val data = list.get(position)
            titleTv.text = data.article_title
            userNameTv.text = data.opinion_title
            if (data.opinion_logo != null && !data.opinion_logo.equals("null")) {
                Helper.loadImage(userImageView, data.opinion_logo!!, "")
            }

            itemContainer.setOnClickListener {
                if (null != homeItemClick) {
                    homeItemClick.itemHomeClick(data)
                }
            }
        }
    }

    abstract class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        abstract fun getBind(position: Int, viewHolder: ViewHolder)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.opinion_horizontal_item_child, parent, false);
        return SimpleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.getBind(position, viewHolder = holder)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}