package com.tv9news.models.home

data class DataHome(
    val id: String,
    val layout_title: String,
    val layout_code: String,
    val details: Details,
    val list: List<Lists>,
    val sub_categories: List<SubCategory>
)