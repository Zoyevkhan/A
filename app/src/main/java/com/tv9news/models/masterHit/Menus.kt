package com.tv9news.models.masterHit


data class Menus(
    val id: String,
    val title: String,
    val icon: String,
    val type: String,
    val menu_action_element: String,
    val position: String,
    val menu_side: String,
    val created_on: String,
    val app_id: String,
    val sub_category: List<SubCategory>
): java.io.Serializable