package com.tv9news.details.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tv9news.R
import com.tv9news.details.interfaces.AuthorClick
import com.tv9news.models.authors.AuthorDetailList
import com.tv9news.utils.helpers.Helper

class MoreAuthorAdapter(val list: List<AuthorDetailList>, val context: Context, val authorClick: AuthorClick) :
    RecyclerView.Adapter<MoreAuthorAdapter.ViewHolder>() {

    inner class SimpleViewHolder(v: View) : ViewHolder(v) {
        val viewProfileTv = v.findViewById<TextView>(R.id.viewProfileTv)
        val authorNameTv = v.findViewById<TextView>(R.id.authorNameTv)
        val authorFromTv = v.findViewById<TextView>(R.id.authorFromTv)
        val imageView = v.findViewById<ImageView>(R.id.imageView)


        override fun getBind(position: Int, viewHolder: ViewHolder) {
            val data = list.get(position)
            authorNameTv.text = data.author_name
            authorFromTv.text = data.designation
            if (data.author_logo != null && !data.author_logo.equals("null")) {
                Helper.loadImage(imageView, data.author_logo, "")
            }

            viewProfileTv.setOnClickListener {
                authorClick.AuthorClick(data.id)
            }
        }
    }

    abstract class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        abstract fun getBind(position: Int, viewHolder: ViewHolder)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.author_item_view_child, parent, false);
        return SimpleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.getBind(position, viewHolder = holder)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}