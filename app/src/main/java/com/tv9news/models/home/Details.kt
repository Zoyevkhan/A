package com.tv9news.models.home

data class Details(
    val id: String,
    val cat_ids: String,
    val article_title: String,
    val article_type: String,
    val article_description: String,
    val article_excerpt: String,
    val media_url: String,
    val duration: String,
    val article_image: String,
    val posted_on: String,
    val author: String,
    val edited_by: String,
    val category_name: String,
    val tags: String
)
