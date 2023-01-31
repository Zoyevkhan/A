package com.tv9news.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.tv9news.models.masterHit.Language

@Dao
interface LanguageDao {



    @Insert
    fun insertLanguage(language: Language)

    @Query("SELECT * FROM language")
    fun getLanguage(): List<Language>
    @Query("DELETE FROM language")
    fun nukeTable()
    @Delete
    fun deleteLanguage(language: Language)


}