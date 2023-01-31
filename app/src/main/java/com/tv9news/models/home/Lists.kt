package com.tv9news.models.home

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Lists(
    var position: Int? = -1,
    var id: String? = null,
    var opinion_title: String? = null,
    var opinion_logo: String? = null,
    var description: String? = null,
    var author_name: String? = null,
    var category_name: String? = null,
    var article_title: String? = null,
    var article_description: String? = null,
    var category_id: String? = null,
    var article_image: String? = null,
    var article_type: String? = null,
    var total_story_slide: String? = null,
    var media_url: String? = null,
    var tag: String? = null,
    var slug: String? = null,
    var widget_title: String? = null,
    var widget_icon: String? = null,
    var duration: String? = null,
    var tags: String? = null,
    var created: String? = null,
    var created_on: String? = null,
    var posted_on: String? = null
): Parcelable