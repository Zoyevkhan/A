package com.tv9news.models.masterHit


data class Category(
    val id: String,
    val lang_id: String,
    val category_title: String,
    val category_logo: String,
    val logo_sizes: String,
    var isSelected: Boolean
): java.io.Serializable