package com.tv9news.models.home

data class HomeApi(
    val status: Boolean,
    val message: String,
    val data: List<DataHome>
)