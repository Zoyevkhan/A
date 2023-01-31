package com.tv9news.home.activities.adapters

import android.content.Context
import android.os.Build
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.tv9news.home.bannercards.BannerLayout
import com.tv9news.home.bannercards.BaseBannerAdapter
import com.tv9news.R
import com.tv9news.home.adapters.*
import com.tv9news.home.interfaces.HomeItemClick
import com.tv9news.models.home.BannerData
import com.tv9news.models.home.DataHome
import com.tv9news.utils.helpers.Constants
import com.tv9news.utils.helpers.Helper


class MainNewsAdapter(
    val list: List<DataHome>,
    val context: Context,
    val homeItemClick: HomeItemClick
) :
    RecyclerView.Adapter<MainNewsAdapter.ViewHolder>() {

    inner class TopNineViewHolder(v: View) : ViewHolder(v) {
        val titleTv = v.findViewById<TextView>(R.id.titleTv)
        val topnineRecyclerView = v.findViewById<RecyclerView>(R.id.topnineRecyclerView)
        override fun getBind(position: Int, viewHolder: ViewHolder) {
            val data = list.get(position)
            titleTv.text = data.layout_title
            topnineRecyclerView.adapter = TopNineNewsItemAdapter(data.list, context, homeItemClick)
            topnineRecyclerView.isNestedScrollingEnabled = false
            topnineRecyclerView.setHasFixedSize(true)
        }
    }

    inner class VerticalLayoutMain(v: View) : ViewHolder(v) {
        val childRecyclerView = v.findViewById<RecyclerView>(R.id.childRecyclerView)
        val topImageView = v.findViewById<ImageView>(R.id.topImageView)
        val titleTv = v.findViewById<TextView>(R.id.titleTv)
        val politicsTv = v.findViewById<TextView>(R.id.politicsTv)
        val timeTv = v.findViewById<TextView>(R.id.timeTv)
        val detailsTv = v.findViewById<TextView>(R.id.detailsTv)
        override fun getBind(position: Int, viewHolder: ViewHolder) {
            val data = list[position]
            titleTv.text = data.layout_title
            if (data.list.isNotEmpty()) {
                val item = data.list[0]
                if (item.article_image != null && !item.article_image.equals("null")) {
                    Helper.loadImage(topImageView, item.article_image!!, "")
                }
                politicsTv.text = item.category_name
                if (item.article_description != null) {
                    detailsTv.text = Helper.fromHtml(item.article_description)
                }
            }
            childRecyclerView.adapter = VerticalLayoutItemAdapter(data.list, context, homeItemClick)
            childRecyclerView.isNestedScrollingEnabled = false
            childRecyclerView.setHasFixedSize(true)
        }
    }

    inner class ImportantTalkLayoutMain(v: View) : ViewHolder(v) {
        val childRecyclerView = v.findViewById<RecyclerView>(R.id.childRecyclerView)
        val mainHeadingTv = v.findViewById<TextView>(R.id.mainHeadingTv)
        val topImageView = v.findViewById<ImageView>(R.id.topImageView)
        val titleTv = v.findViewById<TextView>(R.id.titleTv)
        val politicsTv = v.findViewById<TextView>(R.id.politicsTv)
        val timeTv = v.findViewById<TextView>(R.id.timeTv)

        override fun getBind(position: Int, viewHolder: ViewHolder) {
            val data = list[position]
            mainHeadingTv.text = data.layout_title
            if (data.list.isNotEmpty()) {
                val item = data.list[0]
                if (item.article_image != null && !item.article_image.equals("null")) {
                    Helper.loadImage(topImageView, item.article_image!!, "")
                }
                politicsTv.text = item.category_name
                titleTv.text = item.article_title
            }
            childRecyclerView.adapter = VerticalLayoutItemAdapter(data.list, context, homeItemClick)
            childRecyclerView.isNestedScrollingEnabled = false
            childRecyclerView.setHasFixedSize(true)
        }
    }

    inner class ForYouVerticalMain(v: View) : ViewHolder(v) {
        val childRecyclerView = v.findViewById<RecyclerView>(R.id.childRecyclerView)
        val mainHeadingTv = v.findViewById<TextView>(R.id.mainHeadingTv)
        val subHeadTv = v.findViewById<TextView>(R.id.subHeadTv)
        override fun getBind(position: Int, viewHolder: ViewHolder) {
            val data = list[position]
            mainHeadingTv.text = data.layout_title
//            subHeadTv.text = data.layout_title
            childRecyclerView.adapter = ForYouVerticalItemAdapter(data.list, context, homeItemClick)
            childRecyclerView.isNestedScrollingEnabled = false
            childRecyclerView.setHasFixedSize(true)
        }
    }

    inner class HorizontalWebStoriesMain(v: View) : ViewHolder(v) {
        val childRecyclerView = v.findViewById<RecyclerView>(R.id.childRecyclerView)
        val mainHeadingTv = v.findViewById<TextView>(R.id.mainHeadingTv)
        val arrowImage = v.findViewById<ImageView>(R.id.arrowImage)
        override fun getBind(position: Int, viewHolder: ViewHolder) {
            val data = list[position]
            mainHeadingTv.text = data.layout_title
            childRecyclerView.adapter =
                HorizontalWebStoriesItemAdapter(data.list, context, homeItemClick)
            childRecyclerView.isNestedScrollingEnabled = false
            childRecyclerView.setHasFixedSize(true)
        }
    }

    inner class GridPortraitListMain(v: View): ViewHolder(v){
        val childRecyclerView = v.findViewById<RecyclerView>(R.id.childRecyclerView)
        val mainHeadingTv = v.findViewById<TextView>(R.id.mainHeadingTv)
        override fun getBind(position: Int, viewHolder: ViewHolder) {
            val data = list[position]
//            mainHeadingTv.text = data.layout_title
            val layoutManager = GridLayoutManager(context, 2)
            childRecyclerView.layoutManager = layoutManager
            childRecyclerView.adapter = GridPortraitItemAdapter(
                data.list,
                listOf("1", "1", "1", "1", "1", "1"),
                context,
                homeItemClick
            )
            childRecyclerView.isNestedScrollingEnabled = false
            childRecyclerView.setHasFixedSize(true)
        }
    }

    inner class HorizontalOpinionMain(v: View) : ViewHolder(v) {
        val childRecyclerView = v.findViewById<RecyclerView>(R.id.childRecyclerView)
        val mainHeadingTv = v.findViewById<TextView>(R.id.mainHeadingTv)
        override fun getBind(position: Int, viewHolder: ViewHolder) {
            val data = list[position]
            mainHeadingTv.text = data.layout_title
            childRecyclerView.adapter =
                HorizontalOpinionItemAdapter(data.list, context, homeItemClick)
            childRecyclerView.isNestedScrollingEnabled = false
            childRecyclerView.setHasFixedSize(true)
        }
    }


    inner class HorizontalPodcastItemMain(v: View): ViewHolder(v) {
        val childRecyclerView = v.findViewById<RecyclerView>(R.id.childRecyclerView)
        val mainHeadingTv = v.findViewById<TextView>(R.id.mainHeadingTv)
        override fun getBind(position: Int, viewHolder: ViewHolder) {
            val data = list[position]
            mainHeadingTv.text = data.layout_title
            childRecyclerView.adapter =
                HorizontalPodcastItemAdapter(data.list, context, homeItemClick)
            childRecyclerView.isNestedScrollingEnabled = false
            childRecyclerView.setHasFixedSize(true)
        }
    }
        inner class VerticalCatDetailsItemMain(v: View): ViewHolder(v){
        val titleRecyclerView = v.findViewById<RecyclerView>(R.id.titleRecyclerView)
        val childRecyclerView = v.findViewById<RecyclerView>(R.id.childRecyclerView)
        val mainHeadingTv = v.findViewById<TextView>(R.id.mainHeadingTv)
        val titleTv = v.findViewById<TextView>(R.id.titleTv)
        val politicsTv = v.findViewById<TextView>(R.id.politicsTv)
        val timeTv = v.findViewById<TextView>(R.id.timeTv)
        val topImageView = v.findViewById<ImageView>(R.id.topImageView)
        override fun getBind(position: Int, viewHolder: ViewHolder) {
            val data = list[position]
            mainHeadingTv.text = data.layout_title
            if (data.list.isNotEmpty()) {
                val item = data.list[0]
                if (item.article_image != null && !item.article_image.equals("null")) {
                    Helper.loadImage(topImageView, item.article_image!!, "")
                }
                politicsTv.text = item.category_name
                titleTv.text = item.article_title
            }

            titleRecyclerView.adapter =
                VerticalTopCatItemAdapter(data.sub_categories, context, homeItemClick)
            titleRecyclerView.isNestedScrollingEnabled = false
            titleRecyclerView.setHasFixedSize(true)

            childRecyclerView.adapter = ForYouVerticalItemAdapter(data.list, context, homeItemClick)
            childRecyclerView.isNestedScrollingEnabled = false
            childRecyclerView.setHasFixedSize(true)
        }

    }
    inner class HorizontalCatTabMain(v: View) : ViewHolder(v) {
        val titleRecyclerView = v.findViewById<RecyclerView>(R.id.titleRecyclerView)
        val childRecyclerView = v.findViewById<RecyclerView>(R.id.childRecyclerView)
        val mainHeadingTv = v.findViewById<TextView>(R.id.headingMainTv)
        val titleTv = v.findViewById<TextView>(R.id.titleTv)
        val politicsTv = v.findViewById<TextView>(R.id.politicsTv)
        val timeTv = v.findViewById<TextView>(R.id.timeTv)
        val topImageView = v.findViewById<ImageView>(R.id.topImageView)

        override fun getBind(position: Int, viewHolder: ViewHolder) {
            val data = list[position]
            mainHeadingTv.text = data.layout_title
            if (data.list.isNotEmpty()) {
                val item = data.list[0]
                if (item.article_image != null && !item.article_image.equals("null")) {
                    Helper.loadImage(topImageView, item.article_image!!, "")
                }
                politicsTv.text = item.category_name
                titleTv.text = item.article_title
            }
            if (data.sub_categories != null) {
                titleRecyclerView.adapter =
                    HorizontalPhotoTabItemAdapter(data.sub_categories, context, homeItemClick)
                titleRecyclerView.isNestedScrollingEnabled = false
                titleRecyclerView.setHasFixedSize(true)
            }

            childRecyclerView.adapter =
                HorizontalCatTabItemAdapter(data.list, context, homeItemClick)
            childRecyclerView.isNestedScrollingEnabled = false
            childRecyclerView.setHasFixedSize(true)
        }
    }

    inner class HorizontalVideoBannerMain(v: View) : ViewHolder(v) {
        val childRecyclerView = v.findViewById<RecyclerView>(R.id.childRecyclerView)
        val mainHeadingTv = v.findViewById<TextView>(R.id.mainHeadingTv)
        val videoDurationTv = v.findViewById<TextView>(R.id.videoDurationTv)
        val titleTv = v.findViewById<TextView>(R.id.titleTv)
        val mainImageView = v.findViewById<ImageView>(R.id.mainImageView)
        override fun getBind(position: Int, viewHolder: ViewHolder) {
            val data = list[position]
            mainHeadingTv.text = data.layout_title
            if (data.list.isNotEmpty()) {
                val item = data.list[0]
                if (item.article_image != null && !item.article_image.equals("null")) {
                    Helper.loadImage(mainImageView, item.article_image!!, "")
                }
                titleTv.text = item.article_title
//                videoDurationTv.text = item.
            }
            childRecyclerView.adapter =
                HorizontalVideoBannerItemAdapter(data.list, context, homeItemClick)
            childRecyclerView.isNestedScrollingEnabled = false
            childRecyclerView.setHasFixedSize(true)
        }
    }


    inner class PodcastTabItemView(v: View): ViewHolder(v) {
        val imageSlider = v.findViewById<BannerLayout>(R.id.imageBanner)
        val childRecyclerView = v.findViewById<RecyclerView>(R.id.childRecyclerView)

        override fun getBind(position: Int, viewHolder: ViewHolder) {
            val list = listOf("1", "1", "1", "1", "1", "1", "1", "1")
            childRecyclerView.adapter = VerticalPodcastItemAdapter(list, context, homeItemClick)
            childRecyclerView.isNestedScrollingEnabled = false
            childRecyclerView.setHasFixedSize(true)

            val imageList: MutableList<BannerData> = ArrayList()
            imageList.add(BannerData(""))
            imageList.add(BannerData("https://i.ibb.co/sCnMHNN/Drishti-live-app-banner.png"))
            imageList.add(BannerData("https://i.ibb.co/sCnMHNN/Drishti-live-app-banner.png"))
            createBanners(imageList)
        }

        fun createBanners(bannerResponse: MutableList<BannerData>) {
            val imageList: MutableList<String> = ArrayList()
            if (bannerResponse == null) {
                imageSlider.setVisibility(View.GONE)
                return
            }
            if (bannerResponse.size <= 0) {
                imageSlider.setVisibility(View.GONE)
                return
            }
            for (bannerData in bannerResponse) {
                imageList.add(if (bannerData.imageLink == null) "https://i.ibb.co/sCnMHNN/Drishti-live-app-banner.png" else bannerData.imageLink)
            }
            imageSlider.setShowIndicator(false)
            imageSlider.setAutoPlaying(true)
            imageSlider.setAutoPlayDuration(3000)
            val webBannerAdapter = PodcastBannerAdapter(context, imageList)
            imageSlider.setAdapter(webBannerAdapter)
            webBannerAdapter.setOnBannerItemClickListener { i ->
                try {
                    val bannerData: BannerData? = bannerResponse[i]
                    /*String linkLavel= bannerData.getLinkLevel()
                    String typeLink= bannerData.getTypeLink()
                    String studyTypeId= bannerData.getStudyType()
                    String studyTypeDetailId= bannerData.getStudeTypeDetail()
//                    String feedTypeId= bannerData.getFeedType()*/
//                    if (mListener != null) {
//                        try {
//                            mListener.onHomeBannerItemClick(bannerData)
//                        } catch (e: Exception) {
//                            e.printStackTrace()
//                        }
//                    }
                } catch (e: Exception) {
                    Log.e("TAG", "error", e)
                }
            }
        }

    }


    inner class HorizontalPhotoTabMain(v: View) : ViewHolder(v) {
        val titleRecyclerView = v.findViewById<RecyclerView>(R.id.titleRecyclerView)
        val imageSlider = v.findViewById<BannerLayout>(R.id.imageBanner)
        val headingMainTv = v.findViewById<TextView>(R.id.headingMainTv)
        override fun getBind(position: Int, viewHolder: ViewHolder) {
            val data = list[position]
            headingMainTv.text = data.layout_title
            titleRecyclerView.adapter = HorizontalPhotoTabItemAdapter(
                data.sub_categories,
                context,
                homeItemClick
            )
            titleRecyclerView.isNestedScrollingEnabled = false
            titleRecyclerView.setHasFixedSize(true)
            val imageList: MutableList<BannerData> = ArrayList()
            imageList.add(BannerData(""))
            imageList.add(BannerData("https://i.ibb.co/sCnMHNN/Drishti-live-app-banner.png"))
            imageList.add(BannerData("https://i.ibb.co/sCnMHNN/Drishti-live-app-banner.png"))
            createBanners(imageList)
        }

        fun createBanners(bannerResponse: MutableList<BannerData>) {
            val imageList: MutableList<String> = ArrayList()
            if (bannerResponse == null) {
                imageSlider.setVisibility(View.GONE)
                return
            }
            if (bannerResponse.size <= 0) {
                imageSlider.setVisibility(View.GONE)
                return
            }
            for (bannerData in bannerResponse) {
                imageList.add(if (bannerData.imageLink == null) "https://i.ibb.co/sCnMHNN/Drishti-live-app-banner.png" else bannerData.imageLink)
            }
            imageSlider.setShowIndicator(false)
            imageSlider.setAutoPlaying(true)
            imageSlider.setAutoPlayDuration(3000)
            val webBannerAdapter = BaseBannerAdapter(context, imageList)
            imageSlider.setAdapter(webBannerAdapter)
            webBannerAdapter.setOnBannerItemClickListener { i ->
                try {
                    val bannerData: BannerData? = bannerResponse[i]
                    /*String linkLavel= bannerData.getLinkLevel()
                    String typeLink= bannerData.getTypeLink()
                    String studyTypeId= bannerData.getStudyType()
                    String studyTypeDetailId= bannerData.getStudeTypeDetail()
//                    String feedTypeId= bannerData.getFeedType()*/
//                    if (mListener != null) {
//                        try {
//                            mListener.onHomeBannerItemClick(bannerData)
//                        } catch (e: Exception) {
//                            e.printStackTrace()
//                        }
//                    }
                } catch (e: Exception) {
                    Log.e("TAG", "error", e)
                }
            }
        }
    }


    inner class GridTopicsMain(v: View) : ViewHolder(v) {
        val childRecyclerView = v.findViewById<RecyclerView>(R.id.recyclerView)
        val headingMainTv = v.findViewById<TextView>(R.id.headingMainTv)

        override fun getBind(position: Int, viewHolder: ViewHolder) {
            val data = list[position]
            headingMainTv.text = data.layout_title
            val layoutManager = FlexboxLayoutManager(context)
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.FLEX_START
            childRecyclerView.layoutManager = layoutManager

            val adapter = GridTopicsItemAdapter(data.list, homeItemClick)
            childRecyclerView.adapter = adapter
        }
    }

    inner class EmptyItemViewHolder(v: View): ViewHolder(v){
        override fun getBind(position: Int, viewHolder: ViewHolder) {
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
            val adapter = GridTrendTopicItemAdapter(data.list, homeItemClick)
            childRecyclerView.adapter = adapter

        }
    }



    inner class GridWidgetItemMain(v: View) : ViewHolder(v) {
        val childRecyclerView = v.findViewById<RecyclerView>(R.id.recyclerView)
        override fun getBind(position: Int, viewHolder: ViewHolder) {
            val data = list[position]
            val layoutManagerMemes = GridLayoutManager(context, 2)
            childRecyclerView.layoutManager = layoutManagerMemes
            val adapter = GridWidgetItemAdapter(data.list, homeItemClick)
            childRecyclerView.adapter = adapter
        }
    }

    inner class PhotoGalleryItemMain(v: View) : ViewHolder(v) {
        val childRecyclerView = v.findViewById<RecyclerView>(R.id.childRecyclerView)
        override fun getBind(position: Int, viewHolder: ViewHolder) {
            val data = list[position]
            childRecyclerView.adapter = PhotoGalleryItemAdapter(data.list, context, homeItemClick)
            childRecyclerView.isNestedScrollingEnabled = false
            childRecyclerView.setHasFixedSize(true)
        }
    }

    inner class ViralPhotoGalleryItemMain(v: View) : ViewHolder(v) {
        val childRecyclerView = v.findViewById<RecyclerView>(R.id.childRecyclerView)
        override fun getBind(position: Int, viewHolder: ViewHolder) {
            val data = list[position]
            childRecyclerView.adapter =
                ViralPhotoGalleryItemAdapter(data.list, context, homeItemClick)
            childRecyclerView.isNestedScrollingEnabled = false
            childRecyclerView.setHasFixedSize(true)
        }
    }

    inner class VerticalVideoItemView(v: View) : ViewHolder(v) {
        val childRecyclerView = v.findViewById<RecyclerView>(R.id.childRecyclerView)
        override fun getBind(position: Int, viewHolder: ViewHolder) {
//            val data = list[position]
            val list = listOf("1", "2", "3", "4", "5", "6", "7")
            childRecyclerView.adapter = VerticalVideoItemAdapter(list, context, homeItemClick)
            childRecyclerView.isNestedScrollingEnabled = false
            childRecyclerView.setHasFixedSize(true)
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
                        .inflate(R.layout.top_nine_view_main_layout, parent, false)
                return TopNineViewHolder(view)
            }
            2 -> {
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.vertical_layout_main_view, parent, false)
                return VerticalLayoutMain(view)
            }
            3 -> {
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.vertical_important_talk_main_view, parent, false)
                return ImportantTalkLayoutMain(view)
            }
            4 -> {
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.vertical_for_you_main_view, parent, false)
                return ForYouVerticalMain(view)
            }
            5 -> {
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.grid_topics_main_view, parent, false)
                return GridTopicsMain(view)
            }
            6 -> {
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.horizontal_webstories_item_main, parent, false)
                return HorizontalWebStoriesMain(view)
            }
            7 -> {
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.opinion_horizontal_item_main, parent, false)
                return HorizontalOpinionMain(view)
            }
            8 -> {
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.horizontal_photos_tab_item_main, parent, false)
                return HorizontalPhotoTabMain(view)
            }
            9 -> {
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.horizontal_cat_banner_item_main, parent, false)
                return HorizontalCatTabMain(view)
            }
            10 -> {
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.horizontal_video_banner_item_main, parent, false)
                return HorizontalVideoBannerMain(view)
            }
            11 -> {
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.grid_trend_topic_item_main, parent, false)
                return GridTrendTopicMain(view)
            }
            12 ->{
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.grid_widgets_item_main, parent, false)
                return GridWidgetItemMain(view)
            }
            13 ->{
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.vertical_cat_details_item_main, parent, false)
                return VerticalCatDetailsItemMain(view)
            }
            14 ->{
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.horizontal_podcast_item_main, parent, false)
                return HorizontalPodcastItemMain(view)
            }
            15->{
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.horizontal_photo_gallery_item_main, parent, false)
                return PhotoGalleryItemMain(view)
            } 16->{
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.viral_photo_gallery_item_main, parent, false)
                return ViralPhotoGalleryItemMain(view)
            } 17 -> {
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.video_tab_item_main, parent, false)
                return VerticalVideoItemView(view)
            }18 -> {
            val view =
                LayoutInflater.from(context)
                    .inflate(R.layout.vertical_podcast_item_main, parent, false)
            return PodcastTabItemView(view)
        }19 -> {
            val view =
                LayoutInflater.from(context)
                    .inflate(R.layout.grid_portrait_list, parent, false)
            return GridPortraitListMain(view)
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
//        Log.i("MainDATAADapter", list.get(position).toString())
    }

    override fun getItemViewType(position: Int): Int {
        if (list[position].layout_code != null) {
            when (list[position].layout_code) {
                Constants.HORIZONTAL_TOP -> return 1
                Constants.VERTICAL_DETAIL -> return 2
                Constants.VERTICAL_IMAGE_LIST -> return 3
                Constants.VERTICAL_FOR_YOU -> return 4
                Constants.GRID_TOPICS -> return 5
                Constants.HORIZONTAL_WEB_STORY -> return 6
                Constants.HORIZONTAL_OPINION -> return 7
                Constants.HORIZONTAL_PHOTOS_TAB -> return 8
                Constants.HORIZONTAL_CAT_BANNER -> return 9
                Constants.HORIZONTAL_VIDEO_BANNER -> return 10
                Constants.GRID_TREND_TOPICS -> return 11
                Constants.GRID_WIDGETS -> return 12
                Constants.VERTICAL_CAT_DETAIL -> return 13
                Constants.HORIZONTAL_PODCAST -> return 14
                Constants.HORIZONTAL_IMAGE_BANNER -> return 15
                Constants.GRID_IMAGE_CARD -> return 16
                Constants.VERTICAL_VIDEO -> return 17
                Constants.PODCAST_VERTICAL_LIST -> return 18
                Constants.GRID_PORTRAIT_LIST -> return 19
            }
        }
        return 0
    }


    override fun getItemCount(): Int {
        return list.size
    }
}