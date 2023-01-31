package com.tv9news.models.masterHit


data class State(
    val id: String,
    val lang_id: String,
    val state_name: String,
    val state_logo: String,
    val logo_sizes: String,
    var isSelected: Boolean
): java.io.Serializable