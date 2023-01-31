package com.tv9news.details.adapters

import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tv9news.R
import com.tv9news.details.interfaces.OnPodcastClickListener
import com.tv9news.models.home.Lists

class PodcastAudioAdapter(
    val jcAudioList: List<Lists>?,
    var mListener: OnPodcastClickListener
) :
    RecyclerView.Adapter<PodcastAudioAdapter.PodcastAudioAdapterViewHolder>() {
    private val progressMap = SparseArray<Float>()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PodcastAudioAdapterViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.podcast_audio_item, parent, false)
        return PodcastAudioAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: PodcastAudioAdapterViewHolder, position: Int) {
        val title = jcAudioList!![position].article_title
        holder.audioTitle.text = title
        holder.itemView.tag = jcAudioList.get(position)

        holder.itemView.setOnClickListener(View.OnClickListener {
            if (mListener != null)
                mListener!!.onItemClick(position)
        })
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return jcAudioList?.size ?: 0
    }

    fun updateProgress(podcastAudio: Lists, progress: Float) {
        val position = jcAudioList!!.indexOf(podcastAudio)
        progressMap.put(position, progress)
        if (progressMap.size() > 1) {
            for (i in 0 until progressMap.size()) {
                if (progressMap.keyAt(i) != position) {
                    notifyItemChanged(progressMap.keyAt(i))
                    progressMap.delete(progressMap.keyAt(i))
                }
            }
        }
        notifyItemChanged(position)
    }

    class PodcastAudioAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val audioTitle: TextView

        init {
            audioTitle = view.findViewById(R.id.titleTv)
        }
    }
}