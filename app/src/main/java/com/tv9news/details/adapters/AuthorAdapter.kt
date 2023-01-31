package com.tv9news.details.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tv9news.R
import com.tv9news.details.interfaces.AuthorClick
import com.tv9news.models.authors.AuthorDataList
import com.tv9news.models.authors.AuthorListData

class AuthorAdapter(val list: List<AuthorDataList>, val context: Context, val authorClick: AuthorClick) :
    RecyclerView.Adapter<AuthorAdapter.ViewHolder>() {

    inner class SimpleViewHolder(v: View) : ViewHolder(v) {
        val titleTv = v.findViewById<TextView>(R.id.titleTv)
        val recyclerView = v.findViewById<RecyclerView>(R.id.recyclerView)

        override fun getBind(position: Int, viewHolder: ViewHolder) {
            val data = list.get(position)
            titleTv.text = data.layout_title

            val layoutManager = GridLayoutManager(context, 2)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = AuthorItemAdapter(data.list, context, authorClick)
            recyclerView.isNestedScrollingEnabled = false
            recyclerView.setHasFixedSize(true)

        }
    }

    abstract class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        abstract fun getBind(position: Int, viewHolder: ViewHolder)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.author_item_view_main, parent, false);
        return SimpleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.getBind(position, viewHolder = holder)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}