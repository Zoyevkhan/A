package com.tv9news.models.masterHit

data class Menu(
    val top_menu: List<Menus>,
    val left_menu: List<Menus>,
    val bottom_menu: List<Menus>,
    val left_top_menu: List<Menus>
) : java.io.Serializable
