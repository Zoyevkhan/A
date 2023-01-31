package com.tv9news.details.adapters

import android.content.Context
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.tv9news.R
import com.tv9news.details.interfaces.AuthorClick
import com.tv9news.models.authors.AuthorDetailsData
import com.tv9news.utils.helpers.Helper

class AuthorDetailsAdapter (val list: List<AuthorDetailsData>, val context: Context, val authorClick: AuthorClick) :
    RecyclerView.Adapter<AuthorDetailsAdapter.ViewHolder>() {

    inner class SimpleViewHolder(v: View) : ViewHolder(v) {
        val authorNameTv = v.findViewById<TextView>(R.id.authorNameTv)
        val authorFromTv = v.findViewById<TextView>(R.id.authorFromTv)
        val descriptionTv = v.findViewById<TextView>(R.id.descriptionTv)
        val imageView = v.findViewById<ShapeableImageView>(R.id.imageView)
        override fun getBind(position: Int, viewHolder: ViewHolder) {
            val data = list.get(position).details
            authorNameTv.text = data.author_name
            authorFromTv.text = data.designation
            descriptionTv.text = Helper.fromHtml(data.description)
            if (data.author_logo != null) {
                Helper.loadImage(imageView, data.author_logo, "")
            }
        }
    }

    inner class MoreAuthorViewHolder(v: View) : ViewHolder(v) {
                val titleTv = v.findViewById<TextView>(R.id.titleTv)
        val recyclerView = v.findViewById<RecyclerView>(R.id.recyclerView)
        override fun getBind(position: Int, viewHolder: ViewHolder) {
            val data = list.get(position)
            titleTv.text = data.layout_title
            recyclerView.adapter = MoreAuthorAdapter(data.list, context, authorClick)
            recyclerView.isNestedScrollingEnabled = false
            recyclerView.setHasFixedSize(true)

        }
    }

    inner class NewsViewHolder(v: View) : ViewHolder(v) {
        val recyclerView = v.findViewById<RecyclerView>(R.id.recyclerView)
        override fun getBind(position: Int, viewHolder: ViewHolder) {
            val data = list.get(position)
            recyclerView.adapter = AuthorDetailNewsAdapter(data.list, context)
            recyclerView.isNestedScrollingEnabled = false
            recyclerView.setHasFixedSize(true)
        }
    }

    inner class EmptyItemViewHolder(v: View): ViewHolder(v){
        override fun getBind(position: Int, viewHolder: ViewHolder) {
        }
    }

    abstract class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        abstract fun getBind(position: Int, viewHolder: ViewHolder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        when (viewType) {
            0 -> {
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.author_details_item_main, parent, false);
                return SimpleViewHolder(view)
            }
            1 -> {
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.more_author_item_child, parent, false);
                return MoreAuthorViewHolder(view)
            }
            2 -> {
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.author_news_item_view, parent, false);
                return NewsViewHolder(view)
            }
            else -> {
            val view =
                LayoutInflater.from(context)
                    .inflate(R.layout.empty_item_layout, parent, false)
            return EmptyItemViewHolder(view)
        }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.getBind(position, viewHolder = holder)
    }

    override fun getItemViewType(position: Int): Int {
//        if (list[position].layout_code != null) {
//            when (list[position].layout_code) {
//                Constants.HORIZONTAL_TOP -> return 1
//                Constants.VERTICAL_DETAIL -> return 2
//                Constants.VERTICAL_IMAGE_LIST -> return 3
//                Constants.VERTICAL_FOR_YOU -> return 4
//                Constants.GRID_TOPICS -> return 5
//                Constants.HORIZONTAL_WEB_STORY -> return 6
//                Constants.HORIZONTAL_OPINION -> return 7
//                Constants.HORIZONTAL_PHOTOS_TAB -> return 8
//                Constants.HORIZONTAL_CAT_BANNER -> return 9
//                Constants.HORIZONTAL_VIDEO_BANNER -> return 10
//                Constants.GRID_TREND_TOPICS -> return 11
//                Constants.GRID_WIDGETS -> return 12
//                Constants.VERTICAL_CAT_DETAIL -> return 13
//                Constants.HORIZONTAL_PODCAST -> return 14
//                Constants.HORIZONTAL_IMAGE_BANNER -> return 15
//                Constants.GRID_IMAGE_CARD -> return 16
//                Constants.VERTICAL_VIDEO -> return 17
//                Constants.PODCAST_VERTICAL_LIST -> return 18
//                Constants.GRID_PORTRAIT_LIST -> return 19
//            }
//        }
//        return 0
        return position
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
