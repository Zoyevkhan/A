package com.tv9news.home.activities.adapters

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.speech.tts.UtteranceProgressListener
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.tv9news.R
import com.tv9news.databinding.ActivityDetailsBinding
import com.tv9news.details.activities.DetailsActivity
import com.tv9news.details.interfaces.DetailsItemClick
import com.tv9news.models.home.DataHome
import com.tv9news.utils.helpers.Constants
import com.tv9news.utils.helpers.Helper
import com.tv9news.utils.helpers.Helper.htmlToText
import com.tv9news.utils.helpers.Helper.setDateMilis
import com.tv9news.utils.helpers.Helper.showWebData
import java.util.*


class DetailNewsAdapter(
    val list: List<DataHome>,
    val context: Context,
    val detailsItemClick: DetailsItemClick
) :
    RecyclerView.Adapter<DetailNewsAdapter.ViewHolder>() {

    var textListen: TextToSpeech? = null

    inner class DetailPhotoHeaderMain(v: View) : ViewHolder(v) {
        val titleTv = v.findViewById<TextView>(R.id.titleTv)
        val editorLayout = v.findViewById<LinearLayout>(R.id.editorLayout)
        val editedBy = v.findViewById<TextView>(R.id.tvTitle)
        val timeTv = v.findViewById<TextView>(R.id.timeTv)
        val description = v.findViewById<WebView>(R.id.description)

        override fun getBind(position: Int, viewHolder: ViewHolder) {
            val data = list[position]
            titleTv.text = data.details.article_title
            editedBy.text = data.details.edited_by
            timeTv.text = setDateMilis(data.details.posted_on)
            //description.text = fromHtml(data.details.article_description)
            showWebData(context, data.details.article_description, description, Color.WHITE)
            editorLayout.setOnClickListener {
                detailsItemClick.AuthorClick("")
            }
            setFontSpeech(data, description)
        }
    }

    inner class DetailVideoHeaderMain(v: View) : ViewHolder(v) {
        val headLine = v.findViewById<TextView>(R.id.headLine)
        val titleTv = v.findViewById<TextView>(R.id.titleTv)
        val editorLayout = v.findViewById<LinearLayout>(R.id.editorLayout)
        val editedBy = v.findViewById<TextView>(R.id.tvTitle)
        val timeTv = v.findViewById<TextView>(R.id.timeTv)
        val description = v.findViewById<WebView>(R.id.description)

        override fun getBind(position: Int, viewHolder: ViewHolder) {
            val data = list[position]
            headLine.text = data.details.article_excerpt
            titleTv.text = data.details.article_title
            editedBy.text = data.details.edited_by
            timeTv.text = setDateMilis(data.details.posted_on)
            //description.text = fromHtml(data.details.article_description)
            showWebData(context, data.details.article_description, description, Color.WHITE)
            editorLayout.setOnClickListener {
                detailsItemClick.AuthorClick("")
            }
            setFontSpeech(data, description)
        }
    }

    inner class DetailArticleHeaderMain(v: View) : ViewHolder(v) {
        val headLine = v.findViewById<TextView>(R.id.headLine)
        val titleTv = v.findViewById<TextView>(R.id.titleTv)
        val editorLayout = v.findViewById<LinearLayout>(R.id.editorLayout)
        val editedBy = v.findViewById<TextView>(R.id.tvTitle)
        val timeTv = v.findViewById<TextView>(R.id.timeTv)
        val description = v.findViewById<WebView>(R.id.description)
        val topImageView = v.findViewById<ImageView>(R.id.topImageView)

        override fun getBind(position: Int, viewHolder: ViewHolder) {
            val data = list[position]
            headLine.text = data.details.article_excerpt
            titleTv.text = data.details.article_title
            editedBy.text = data.details.edited_by
            timeTv.text = setDateMilis(data.details.posted_on)
            //description.text = fromHtml(data.details.article_description)
            showWebData(context, data.details.article_description, description, Color.WHITE)
            if (!TextUtils.isEmpty(data.details.article_image)) {
                Helper.loadImage(topImageView, data.details.article_image, "")
            }
            editorLayout.setOnClickListener {
                detailsItemClick.AuthorClick("")
            }
            setFontSpeech(data, description)
        }
    }

    inner class DetailMoreListMain(v: View) : ViewHolder(v) {
        val moreRecyclerView = v.findViewById<RecyclerView>(R.id.moreRecyclerView)
        val mainHeadingTv = v.findViewById<TextView>(R.id.mainHeadingTv)

        override fun getBind(position: Int, viewHolder: ViewHolder) {
            val data = list[position]
            mainHeadingTv.text = data.layout_title
            moreRecyclerView.adapter = VerticalMoreItemAdapter(data.list, context, detailsItemClick)
            moreRecyclerView.isNestedScrollingEnabled = false
            moreRecyclerView.setHasFixedSize(true)
        }
    }

    inner class DetailNextStoryMain(v: View) : ViewHolder(v) {
        val storyTitle = v.findViewById<TextView>(R.id.storyTitle)
        val storyDescription = v.findViewById<TextView>(R.id.storyDescription)

        override fun getBind(position: Int, viewHolder: ViewHolder) {
            val data = list[position]
            storyTitle.text = "अगली स्टोरी"
            storyDescription.text =
                "क्यूबाः हवाना के लग्जरी होटल साराटोगा में जोरदार धमाका, 22 लोगों की मौत"
        }
    }

    inner class GridTrendTopicMain(v: View) : ViewHolder(v) {
        val childRecyclerView = v.findViewById<RecyclerView>(R.id.recyclerView)
        val headingMainTv = v.findViewById<TextView>(R.id.headingMainTv)

        override fun getBind(position: Int, viewHolder: ViewHolder) {
            val data = list[position]
            headingMainTv.text = data.layout_title
            val layoutManager = FlexboxLayoutManager(context)
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.FLEX_START
            childRecyclerView.layoutManager = layoutManager
            val adapter = GridTrendItemAdapter(data.list)
            childRecyclerView.adapter = adapter

        }
    }

    inner class EmptyItemViewHolder(v: View) : ViewHolder(v) {
        override fun getBind(position: Int, viewHolder: ViewHolder) {
        }

    }

    abstract class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        abstract fun getBind(position: Int, viewHolder: ViewHolder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        when (viewType) {
            1 -> {
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.detail_article_header_item_main, parent, false)
                return DetailArticleHeaderMain(view)
            }
            2 -> {
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.detail_video_header_item_main, parent, false)
                return DetailVideoHeaderMain(view)
            }
            3 -> {
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.detail_photo_header_item_main, parent, false)
                return DetailPhotoHeaderMain(view)
            }
            5 -> {
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.detail_more_list_item_main, parent, false)
                return DetailMoreListMain(view)
            }
            4 -> {
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.detail_next_story_item_main, parent, false)
                return DetailNextStoryMain(view)
            }
            6 -> {
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.grid_trend_topic_item_main, parent, false)
                return GridTrendTopicMain(view)
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
        if (list[position].layout_code != null) {
            when (list[position].layout_code) {
                Constants.DETAIL_ARTICLE_HEADER -> return 1
                Constants.DETAIL_VIDEO_HEADER -> return 2
                Constants.DETAIL_PHOTO_GALLERY -> return 3
                Constants.DETAIL_NEXT_STORY -> return 4
                Constants.DETAIL_MORE_LIST -> return 5
                Constants.GRID_TREND_TOPICS -> return 6
            }
        }
        return 0
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun onInitTextListen() {
        textListen = TextToSpeech(context, OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result: Int = textListen!!.setLanguage(Locale("hi", "IN"))
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.i("TextToSpeech", "Language Not Supported")
                }
                textListen!!.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        Log.i("TextToSpeech", "On Start")
                    }

                    override fun onDone(utteranceId: String?) {
                        stopTextAudio()
                        Log.i("TextToSpeech", "On Done")
                    }

                    override fun onError(utteranceId: String?) {
                        Log.i("TextToSpeech", "On Error")
                    }
                })
            } else {
                Log.i("TextToSpeech", "Initialization Failed")
            }
        })

    }

    fun setFontSpeech(data: DataHome, description: WebView) {
        onInitTextListen()

        getToolbar().textSpeechIV.setOnClickListener(View.OnClickListener {
            if (textListen != null) {
                if (textListen!!.isSpeaking) {
                    stopTextAudio()
                } else {
                    playTextAudio(data)
                }
            }
        })

        getToolbar().fontIV.setOnClickListener(View.OnClickListener {
            when (description.getSettings().textSize) {
                WebSettings.TextSize.NORMAL -> {
                    description.getSettings().setTextSize(WebSettings.TextSize.LARGER);
                    getToolbar().fontIV.setImageResource(R.drawable.font_top_icon_active)
                }
                WebSettings.TextSize.LARGER -> {
                    description.getSettings().setTextSize(WebSettings.TextSize.LARGEST);
                    getToolbar().fontIV.setImageResource(R.drawable.font_top_icon_active)
                }
                WebSettings.TextSize.LARGEST -> {
                    description.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
                    getToolbar().fontIV.setImageResource(R.drawable.font_top_icon)
                }
                else -> {
                    description.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
                    getToolbar().fontIV.setImageResource(R.drawable.font_top_icon)
                }
            }
        })
    }

    fun playTextAudio(data: DataHome) {
        InitListen(true)
        val toSpeak: String = htmlToText(data.details.article_description)!!
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textListen!!.speak(
                toSpeak,
                TextToSpeech.QUEUE_FLUSH,
                null,
                TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED
            );
        }
    }

    public fun stopTextAudio() {
        InitListen(false)
        if (textListen != null) {
            textListen!!.stop()
        }
    }

    fun InitListen(isListen: Boolean) {
        if (isListen) {
            getToolbar().textSpeechIV.setImageResource(R.drawable.volume_top_icon_active)
        } else {
            getToolbar().textSpeechIV.setImageResource(R.drawable.volume_top_icon)
        }
    }

    fun getToolbar(): ActivityDetailsBinding {
        return (context as DetailsActivity).binding
    }
}