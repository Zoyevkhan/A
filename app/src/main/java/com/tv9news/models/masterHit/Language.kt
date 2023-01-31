package com.tv9news.models.masterHit

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Language(
    @PrimaryKey(autoGenerate = true)
    var primaryKey:Int=0,
    val id: String,
    val lang_name: String,
    val lang_logo: String,
    val logo_sizes: String,
    val lang_meta: LangMeta,
    var category: List<Category>,
    var states: List<State>,
    val menu: Menu,
    var isSelected: Boolean
) : java.io.Serializable