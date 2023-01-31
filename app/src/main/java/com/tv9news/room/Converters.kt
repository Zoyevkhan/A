package com.tv9news.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.tv9news.models.masterHit.Category
import com.tv9news.models.masterHit.LangMeta
import com.tv9news.models.masterHit.Menu
import com.tv9news.models.masterHit.State

class Converters {
    @TypeConverter
    fun fromCategories(categoryList: List<Category>) :String {
        return Gson().toJson(categoryList)
    }
    @TypeConverter
    fun  toCategories(categoryJson: String): List<Category> {
       return Gson().fromJson(categoryJson, Array<Category>::class.java).asList()
    }

    @TypeConverter
    fun fromStates(stateList: List<State>) :String {
        return Gson().toJson(stateList)
    }
    @TypeConverter
    fun  toState(stateJson: String): List<State> {
        return Gson().fromJson(stateJson, Array<State>::class.java).asList()
    }
    @TypeConverter
    fun fromMenu(menu: Menu) :String {
        return Gson().toJson(menu)
    }
    @TypeConverter
    fun  toMenu(menuJson: String): Menu {
        return Gson().fromJson(menuJson, Menu::class.java)
    }

    @TypeConverter
    fun fromLangmeta(langMeta: LangMeta) :String {
        return Gson().toJson(langMeta)
    }
    @TypeConverter
    fun  toLangmeta(langmetaJson: String): LangMeta {
        return Gson().fromJson(langmetaJson, LangMeta::class.java)
    }
}